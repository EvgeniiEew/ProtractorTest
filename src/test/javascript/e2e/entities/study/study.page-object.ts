import { element, by, ElementFinder } from 'protractor';

export class StudyComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css("button[data-cy='entityDeleteButton']"));
  title = element.all(by.css('jhi-study div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class StudyUpdatePage {
  pageTitle = element(by.id('jhi-study-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  dateOfStartInput = element(by.id('field_dateOfStart'));
  dateOfExamInput = element(by.id('field_dateOfExam'));
  gradeInput = element(by.id('field_grade'));
  coursenameSelect = element(by.css("input[formcontrolname='coursename']"));
  studentSelect = element(by.css("input[formcontrolname='student']"));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDateOfStartInput(dateOfStart: string): Promise<void> {
    await this.dateOfStartInput.sendKeys(dateOfStart);
  }

  async getDateOfStartInput(): Promise<string> {
    return await this.dateOfStartInput.getAttribute('value');
  }

  async setDateOfExamInput(dateOfExam: string): Promise<void> {
    await this.dateOfExamInput.sendKeys(dateOfExam);
  }

  async getDateOfExamInput(): Promise<string> {
    return await this.dateOfExamInput.getAttribute('value');
  }

  async setGradeInput(grade: string): Promise<void> {
    await this.gradeInput.sendKeys(grade);
  }

  async getGradeInput(): Promise<string> {
    return await this.gradeInput.getAttribute('value');
  }

  async coursenameSelectLastOption(): Promise<void> {
    await this.coursenameSelect.click();
    let element_study = element(by.id('course 0'));
    await element_study.click();
  }

  async coursenameSelectOption(option: string): Promise<void> {
    await this.coursenameSelect.sendKeys(option);
  }

  getCoursenameSelect(): ElementFinder {
    return this.coursenameSelect;
  }

  async getCoursenameSelectedOption(): Promise<string> {
    return await this.coursenameSelect.element(by.css('option:checked')).getText();
  }

  async studentSelectLastOption(): Promise<void> {
    await this.studentSelect.click();
    let element_student = element(by.id('student 0'));
    await element_student.click();
  }

  async studentSelectOption(option: string): Promise<void> {
    await this.studentSelect.sendKeys(option);
  }

  getStudentSelect(): ElementFinder {
    return this.studentSelect;
  }

  async getStudentSelectedOption(): Promise<string> {
    return await this.studentSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class StudyDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-study-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-study'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
