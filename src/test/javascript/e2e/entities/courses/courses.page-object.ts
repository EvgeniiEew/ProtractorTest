import { element, by, ElementFinder } from 'protractor';

export class CoursesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-courses div table .btn-danger'));
  title = element.all(by.css('jhi-courses div h2#page-heading span')).first();
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

export class CoursesUpdatePage {
  pageTitle = element(by.id('jhi-courses-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  courseNameInput = element(by.id('field_courseName'));
  dateOfStartInput = element(by.id('field_dateOfStart'));
  dateOfEndInput = element(by.id('field_dateOfEnd'));

  nameSelect = element(by.id('field_name'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCourseNameInput(courseName: string): Promise<void> {
    await this.courseNameInput.sendKeys(courseName);
  }

  async getCourseNameInput(): Promise<string> {
    return await this.courseNameInput.getAttribute('value');
  }

  async setDateOfStartInput(dateOfStart: string): Promise<void> {
    await this.dateOfStartInput.sendKeys(dateOfStart);
  }

  async getDateOfStartInput(): Promise<string> {
    return await this.dateOfStartInput.getAttribute('value');
  }

  async setDateOfEndInput(dateOfEnd: string): Promise<void> {
    await this.dateOfEndInput.sendKeys(dateOfEnd);
  }

  async getDateOfEndInput(): Promise<string> {
    return await this.dateOfEndInput.getAttribute('value');
  }

  async nameSelectLastOption(): Promise<void> {
    await this.nameSelect.all(by.tagName('option')).last().click();
  }

  async nameSelectOption(option: string): Promise<void> {
    await this.nameSelect.sendKeys(option);
  }

  getNameSelect(): ElementFinder {
    return this.nameSelect;
  }

  async getNameSelectedOption(): Promise<string> {
    return await this.nameSelect.element(by.css('option:checked')).getText();
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

export class CoursesDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-courses-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-courses'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
