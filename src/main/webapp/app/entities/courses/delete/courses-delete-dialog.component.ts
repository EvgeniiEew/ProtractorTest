import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourses } from '../courses.model';
import { CoursesService } from '../service/courses.service';

@Component({
  templateUrl: './courses-delete-dialog.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class CoursesDeleteDialogComponent {
  courses?: ICourses;

  constructor(protected coursesService: CoursesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coursesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
