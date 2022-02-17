import {browser, by, element, ExpectedConditions as ec, promise} from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CoursesComponentsPage, CoursesDeleteDialog, CoursesUpdatePage } from './courses.page-object';

const expect = chai.expect;

  function sleep(ms: number | undefined) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
describe('Courses e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let coursesComponentsPage: CoursesComponentsPage;
  let coursesUpdatePage: CoursesUpdatePage;
  let coursesDeleteDialog: CoursesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    let input_login =element(by.css("input[id='username']"));
    await input_login.sendKeys(username);
    let input_password =element(by.css("input[formcontrolname='password']"));
    await input_password.sendKeys(password);
    let submit_button = element(by.id('loginButton'));
    await submit_button.click();
    let entityMenu = element(by.id('accordion-header-0'));
    await entityMenu.click();
    let  avirableCourses = element(by.css("div[class='ng-star-inserted']"));
    await avirableCourses.click();
    await sleep(20000);
  });

  it('should load Courses', async () => {
    await navBarPage.goToEntity('courses');
    coursesComponentsPage = new CoursesComponentsPage();
    await browser.wait(ec.visibilityOf(coursesComponentsPage.title), 5000);
    expect(await coursesComponentsPage.getTitle()).to.eq('coursesApp.courses.home.title');
    await browser.wait(ec.or(ec.visibilityOf(coursesComponentsPage.entities), ec.visibilityOf(coursesComponentsPage.noResult)), 1000);
  });

  it('should load create Courses page', async () => {
    await coursesComponentsPage.clickOnCreateButton();
    coursesUpdatePage = new CoursesUpdatePage();
    expect(await coursesUpdatePage.getPageTitle()).to.eq('coursesApp.courses.home.createOrEditLabel');
    await coursesUpdatePage.cancel();
  });

  it('should create and save Courses', async () => {
    const nbButtonsBeforeCreate = await coursesComponentsPage.countDeleteButtons();

    await coursesComponentsPage.clickOnCreateButton();

    await promise.all([
      coursesUpdatePage.setCourseNameInput('courseName'),
      coursesUpdatePage.setDateOfStartInput('2000-12-31'),
      coursesUpdatePage.setDateOfEndInput('2000-12-31'),
      coursesUpdatePage.nameSelectLastOption(),
    ]);

    await coursesUpdatePage.save();
    expect(await coursesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await coursesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Courses', async () => {
    const nbButtonsBeforeDelete = await coursesComponentsPage.countDeleteButtons();
    await coursesComponentsPage.clickOnLastDeleteButton();

    coursesDeleteDialog = new CoursesDeleteDialog();
    expect(await coursesDeleteDialog.getDialogTitle()).to.eq('coursesApp.courses.delete.question');
    await coursesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(coursesComponentsPage.title), 5000);

    expect(await coursesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
