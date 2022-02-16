import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { HttpResponse } from '@angular/common/http';

import { UserService } from '../../user/user.service';
import { IStudent } from '../student.model';

@Component({
  selector: 'jhi-student-detail',
  templateUrl: './student-detail.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class StudentDetailComponent implements OnInit {
  student: IStudent | null = null;

  adminsAccess: any = false;

  constructor(protected activatedRoute: ActivatedRoute, protected userService: UserService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      this.student = student;
    });
  }

  previousState(): void {
    window.history.back();
  }

  setCollomsFromWidth(): string {
    const windowInnerWidth = window.innerWidth;
    if (windowInnerWidth < 480) {
      return 'col-12 ';
    } else {
      return 'col-8';
    }
  }
}
