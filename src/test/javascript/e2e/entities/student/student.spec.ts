import {browser, by, element, ExpectedConditions as ec, promise} from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { StudentComponentsPage, StudentDeleteDialog, StudentUpdatePage } from './student.page-object';

const expect = chai.expect;

describe('Student e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let studentComponentsPage: StudentComponentsPage;
  let studentUpdatePage: StudentUpdatePage;
  let studentDeleteDialog: StudentDeleteDialog;
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

  it('should load Students', async () => {
    await navBarPage.goToEntity('student');
    studentComponentsPage = new StudentComponentsPage();
    await browser.wait(ec.visibilityOf(studentComponentsPage.title), 5000);
    await browser.sleep(1000);
    expect(await studentComponentsPage.getTitle()).to.eq('coursesApp.student.home.title');
    await browser.sleep(1000);
    await browser.wait(ec.or(ec.visibilityOf(studentComponentsPage.entities), ec.visibilityOf(studentComponentsPage.noResult)), 1000);
  });

  it('should load create Student page', async () => {
    await studentComponentsPage.clickOnCreateButton();
    studentUpdatePage = new StudentUpdatePage();
    await browser.sleep(1000);
    await studentUpdatePage.cancel();
  });

  it('should create and save Students', async () => {
    const nbButtonsBeforeCreate = await studentComponentsPage.countDeleteButtons();
    await studentComponentsPage.clickOnCreateButton();
    await studentUpdatePage.setFirstNameInput('firstName'),
    await studentUpdatePage.setLastNameInput('lastName'),
    await studentUpdatePage.setDateOfBirthdayInput('2000-12-31'),
    await browser.sleep(1000);
    await studentUpdatePage.save();
    expect(await studentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Student', async () => {
    await studentComponentsPage.clickOnLastDeleteButton();
    await browser.sleep(1000);
    let confirmButton = element(by.id('jhi-confirm-delete-student'));
    await confirmButton.click();

  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
