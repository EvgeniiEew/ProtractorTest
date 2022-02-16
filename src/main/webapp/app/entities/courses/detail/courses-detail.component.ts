import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { HttpResponse } from '@angular/common/http';

import { UserService } from '../../user/user.service';
import { ICourses } from '../courses.model';

@Component({
  selector: 'jhi-courses-detail',
  templateUrl: './courses-detail.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class CoursesDetailComponent implements OnInit {
  courses: ICourses | null = null;

  adminsAccess: any = false;

  constructor(protected activatedRoute: ActivatedRoute, protected userService: UserService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courses }) => {
      this.courses = courses;
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
