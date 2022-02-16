import { browser, ExpectedConditions as ec, promise } from 'protractor';
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
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Providers', async () => {
    await navBarPage.goToEntity('provider');
    providerComponentsPage = new ProviderComponentsPage();
    await browser.wait(ec.visibilityOf(providerComponentsPage.title), 5000);
    expect(await providerComponentsPage.getTitle()).to.eq('coursesApp.provider.home.title');
    await browser.wait(ec.or(ec.visibilityOf(providerComponentsPage.entities), ec.visibilityOf(providerComponentsPage.noResult)), 1000);
  });

  it('should load create Provider page', async () => {
    await providerComponentsPage.clickOnCreateButton();
    providerUpdatePage = new ProviderUpdatePage();
    expect(await providerUpdatePage.getPageTitle()).to.eq('coursesApp.provider.home.createOrEditLabel');
    await providerUpdatePage.cancel();
  });

  it('should create and save Providers', async () => {
    const nbButtonsBeforeCreate = await providerComponentsPage.countDeleteButtons();

    await providerComponentsPage.clickOnCreateButton();

    await promise.all([providerUpdatePage.setNameInput('name')]);

    await providerUpdatePage.save();
    expect(await providerUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await providerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Provider', async () => {
    const nbButtonsBeforeDelete = await providerComponentsPage.countDeleteButtons();
    await providerComponentsPage.clickOnLastDeleteButton();

    providerDeleteDialog = new ProviderDeleteDialog();
    expect(await providerDeleteDialog.getDialogTitle()).to.eq('coursesApp.provider.delete.question');
    await providerDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(providerComponentsPage.title), 5000);

    expect(await providerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
