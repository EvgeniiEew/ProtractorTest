import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudy } from '../study.model';
import { StudyService } from '../service/study.service';

@Component({
  templateUrl: './study-delete-dialog.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class StudyDeleteDialogComponent {
  study?: IStudy;

  constructor(protected studyService: StudyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
