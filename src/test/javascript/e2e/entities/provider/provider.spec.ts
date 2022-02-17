import {browser, by, element, ExpectedConditions as ec, promise} from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProviderComponentsPage, ProviderDeleteDialog, ProviderUpdatePage } from './provider.page-object';

const expect = chai.expect;

describe('Provider e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let providerComponentsPage: ProviderComponentsPage;
  let providerUpdatePage: ProviderUpdatePage;
  let providerDeleteDialog: ProviderDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    navBarPage = new NavBarPage();
    let input_login = element(by.css("input[id='username']"));
    await input_login.sendKeys(username);
    let input_password = element(by.css("input[formcontrolname='password']"));
    await input_password.sendKeys(password);
    await browser.sleep(1000);
    let submit_button = element(by.id('loginButton'));
    await submit_button.click();
    await browser.sleep(1000);
    let entityMenu = element(by.id('accordion-header-0'));
    await entityMenu.click();
    let avirableCourses = element(by.id('Working data'));
    await avirableCourses.click();
  });

  it('should load Providers', async () => {
    await navBarPage.goToEntity('provider');
    providerComponentsPage = new ProviderComponentsPage();
    await browser.wait(ec.visibilityOf(providerComponentsPage.title), 5000);
    await browser.sleep(1000);
    expect(await providerComponentsPage.getTitle()).to.eq('coursesApp.provider.home.title');
    await browser.sleep(1000);
    await browser.wait(ec.or(ec.visibilityOf(providerComponentsPage.entities), ec.visibilityOf(providerComponentsPage.noResult)), 1000);
  });

  it('should load create Provider page', async () => {
    await providerComponentsPage.clickOnCreateButton();
    providerUpdatePage = new ProviderUpdatePage();
    await browser.sleep(1000);
    await providerUpdatePage.cancel();
  });

  it('should create and save Providers', async () => {
    const nbButtonsBeforeCreate = await providerComponentsPage.countDeleteButtons();

    await providerComponentsPage.clickOnCreateButton();

    await promise.all([providerUpdatePage.setNameInput('name')]);
    await browser.sleep(1000);
    await providerUpdatePage.save();

    expect(await providerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Provider', async () => {
    await providerComponentsPage.clickOnLastDeleteButton();
    await browser.sleep(1000);
    let confirmButton = element(by.id('jhi-confirm-delete-provider'));
    await confirmButton.click();
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
