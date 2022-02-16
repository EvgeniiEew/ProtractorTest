import { browser, ExpectedConditions as ec, promise } from 'protractor';
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
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Studies', async () => {
    await navBarPage.goToEntity('study');
    studyComponentsPage = new StudyComponentsPage();
    await browser.wait(ec.visibilityOf(studyComponentsPage.title), 5000);
    expect(await studyComponentsPage.getTitle()).to.eq('coursesApp.study.home.title');
    await browser.wait(ec.or(ec.visibilityOf(studyComponentsPage.entities), ec.visibilityOf(studyComponentsPage.noResult)), 1000);
  });

  it('should load create Study page', async () => {
    await studyComponentsPage.clickOnCreateButton();
    studyUpdatePage = new StudyUpdatePage();
    expect(await studyUpdatePage.getPageTitle()).to.eq('coursesApp.study.home.createOrEditLabel');
    await studyUpdatePage.cancel();
  });

  it('should create and save Studies', async () => {
    const nbButtonsBeforeCreate = await studyComponentsPage.countDeleteButtons();

    await studyComponentsPage.clickOnCreateButton();

    await promise.all([
      studyUpdatePage.setDateOfStartInput('2000-12-31'),
      studyUpdatePage.setDateOfExamInput('2000-12-31'),
      studyUpdatePage.setGradeInput('5'),
      studyUpdatePage.coursenameSelectLastOption(),
      studyUpdatePage.studentSelectLastOption(),
    ]);

    await studyUpdatePage.save();
    expect(await studyUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await studyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Study', async () => {
    const nbButtonsBeforeDelete = await studyComponentsPage.countDeleteButtons();
    await studyComponentsPage.clickOnLastDeleteButton();

    studyDeleteDialog = new StudyDeleteDialog();
    expect(await studyDeleteDialog.getDialogTitle()).to.eq('coursesApp.study.delete.question');
    await studyDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(studyComponentsPage.title), 5000);

    expect(await studyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
