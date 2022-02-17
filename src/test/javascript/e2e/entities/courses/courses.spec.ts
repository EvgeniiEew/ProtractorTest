import {browser, by, element, ExpectedConditions as ec, promise} from 'protractor';
import {NavBarPage, SignInPage} from '../../page-objects/jhi-page-objects';

import {CoursesComponentsPage, CoursesDeleteDialog, CoursesUpdatePage} from './courses.page-object';

const expect = chai.expect;


describe('Courses e2e test', () => {
  let navBarPage: NavBarPage;
  let coursesComponentsPage: CoursesComponentsPage;
  let coursesUpdatePage: CoursesUpdatePage;
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
    let avirableCourses = element(by.id('Available courses'));
    await avirableCourses.click();

  });

  it('should load Courses', async () => {
    await navBarPage.goToEntity('courses');
    coursesComponentsPage = new CoursesComponentsPage();
    await browser.wait(ec.visibilityOf(coursesComponentsPage.title), 5000);
    await browser.sleep(1000);
    expect(await coursesComponentsPage.getTitle()).to.eq('coursesApp.courses.home.title');
    await browser.sleep(1000);
    await browser.wait(ec.or(ec.visibilityOf(coursesComponentsPage.entities), ec.visibilityOf(coursesComponentsPage.noResult)), 1000);
  });

  it('should load create Courses page', async () => {
    await coursesComponentsPage.clickOnCreateButton();
    coursesUpdatePage = new CoursesUpdatePage();
    await browser.sleep(1000);
    await coursesUpdatePage.cancel();

  });

  it('should create and save Courses', async () => {
    const nbButtonsBeforeCreate = await coursesComponentsPage.countDeleteButtons();
    await coursesComponentsPage.clickOnCreateButton();
    await coursesUpdatePage.setCourseNameInput('courseName');
    await coursesUpdatePage.setDateOfStartInput('2000-12-31');
    await coursesUpdatePage.setDateOfEndInput('2000-12-31');
    await browser.sleep(1000);
    await coursesUpdatePage.nameSelectLastOption();
    await browser.sleep(1000);
    await coursesUpdatePage.save();
    expect(await coursesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Courses', async () => {
    await coursesComponentsPage.clickOnLastDeleteButton();
    await browser.sleep(1000);
    let confirmButton = element(by.id('jhi-confirm-delete-courses'));
    await confirmButton.click();
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
