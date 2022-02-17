import { browser, element, by, ExpectedConditions as ec } from 'protractor';

import { NavBarPage, SignInPage } from '../page-objects/jhi-page-objects';

const expect = chai.expect;

describe('administration', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage(true);
    let input_login =element(by.css("input[id='username']"));
    await input_login.sendKeys(username);
    let input_password =element(by.css("input[formcontrolname='password']"));
    await input_password.sendKeys(password);
    let submit_button = element(by.id('loginButton'));
    await submit_button.click();
    await browser.wait(ec.visibilityOf(navBarPage.adminMenu), 5000);
  });

  beforeEach(async () => {
    await navBarPage.clickOnAdminMenu();
  });

  it('should load user management', async () => {
    await navBarPage.clickOnAdmin('user-management');
    const expect1 = 'userManagement.home.title';
    const value1 = await element(by.id('user-management-page-heading')).getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });

  it('should load metrics', async () => {
    await navBarPage.clickOnAdmin('metrics');
    const heading = element(by.id('metrics-page-heading'));
    await browser.wait(ec.visibilityOf(heading), 10000);
    const expect1 = 'metrics.title';
    const value1 = await heading.getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });

  it('should load health', async () => {
    await navBarPage.clickOnAdmin('health');
    const heading = element(by.id('health-page-heading'));
    await browser.wait(ec.visibilityOf(heading), 10000);
    const expect1 = 'health.title';
    const value1 = await heading.getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });

  it('should load configuration', async () => {
    await navBarPage.clickOnAdmin('configuration');
    const heading = element(by.id('configuration-page-heading'));
    await browser.wait(ec.visibilityOf(heading), 10000);
    const expect1 = 'configuration.title';
    const value1 = await heading.getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });

  it('should load logs', async () => {
    await navBarPage.clickOnAdmin('logs');
    const heading = element(by.id('logs-page-heading'));
    await browser.wait(ec.visibilityOf(heading), 10000);
    const expect1 = 'logs.title';
    const value1 = await heading.getAttribute('jhiTranslate');
    expect(value1).to.eq(expect1);
  });
  after(async () => {
    await navBarPage.autoSignOut();
  });
});
