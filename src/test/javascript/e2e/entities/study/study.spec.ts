import {browser, by, element, ExpectedConditions as ec, promise} from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { StudyComponentsPage, StudyDeleteDialog, StudyUpdatePage } from './study.page-object';

const expect = chai.expect;

describe('Study e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let studyComponentsPage: StudyComponentsPage;
  let studyUpdatePage: StudyUpdatePage;
  let studyDeleteDialog: StudyDeleteDialog;
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

  it('should load Studies', async () => {
    await navBarPage.goToEntity('study');
    studyComponentsPage = new StudyComponentsPage();
    await browser.wait(ec.visibilityOf(studyComponentsPage.title), 5000);
    await browser.sleep(1000);
    expect(await studyComponentsPage.getTitle()).to.eq('coursesApp.study.home.title');
    await browser.sleep(1000);
    await browser.wait(ec.or(ec.visibilityOf(studyComponentsPage.entities), ec.visibilityOf(studyComponentsPage.noResult)), 1000);
  });

  it('should load create Study page', async () => {
    await studyComponentsPage.clickOnCreateButton();
    studyUpdatePage = new StudyUpdatePage();
    await browser.sleep(1000);
    await studyUpdatePage.cancel();
  });

  it('should create and save Studies', async () => {
    const nbButtonsBeforeCreate = await studyComponentsPage.countDeleteButtons();
    await studyComponentsPage.clickOnCreateButton();
     await studyUpdatePage.setDateOfStartInput('2000-12-31'),
     await studyUpdatePage.setDateOfExamInput('2000-12-31'),
     await studyUpdatePage.setGradeInput('5'),
     await studyUpdatePage.coursenameSelectLastOption(),
     await studyUpdatePage.studentSelectLastOption(),
     await browser.sleep(1000);
    await studyUpdatePage.save();
    expect(await studyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Study', async () => {
    await studyComponentsPage.clickOnLastDeleteButton();
    await browser.sleep(1000);
    let confirmButton = element(by.id('jhi-confirm-delete-study'));
    await confirmButton.click();
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
