import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { HttpResponse } from '@angular/common/http';

import { UserService } from '../../user/user.service';
import { IStudy } from '../study.model';

@Component({
  selector: 'jhi-study-detail',
  templateUrl: './study-detail.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class StudyDetailComponent implements OnInit {
  study: IStudy | null = null;

  adminsAccess: any = false;

  constructor(protected activatedRoute: ActivatedRoute, protected userService: UserService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ study }) => {
      this.study = study;
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
