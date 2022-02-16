import { IStudyCriteria } from '../study.criteria';
import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { UserService } from '../../user/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { FormControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourses } from 'app/entities/courses/courses.model';
import { CoursesService } from 'app/entities/courses/service/courses.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

import { IStudy } from '../study.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { StudyService } from '../service/study.service';
import { StudyDeleteDialogComponent } from '../delete/study-delete-dialog.component';
import { Spivot } from '../spivot/spivot';

@Component({
  selector: 'jhi-study',
  templateUrl: './study.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class StudyComponent implements OnInit {
  studies?: IStudy[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  visibleCreateButton = false;
  adminsAccess: any = false;
  criteria: IStudyCriteria;
  sesionValue: any;

  coursesControl = new FormControl();
  coursesList: ICourses[] = [];
  coursesOptions?: Observable<ICourses[]>;
  studentControl = new FormControl();
  studentList: IStudent[] = [];
  studentOptions?: Observable<IStudent[]>;

  constructor(
    protected spivot: Spivot,
    protected studyService: StudyService,
    protected activatedRoute: ActivatedRoute,
    protected userService: UserService,
    protected coursesService: CoursesService,
    protected studentService: StudentService,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    this.criteria = {};
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.studyService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IStudy[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();

    this.sesionValue = sessionStorage.getItem('valinputstudy');
    if (this.sesionValue != null) {
      this.criteria = JSON.parse(this.sesionValue);
      this.filterAndSortEntities(1);
    }

    this.getCourseslist();
    this.getStudentlist();
  }

  getCourseslist(): void {
    this.coursesService
      .query({ sort: ['id'] })
      .pipe(map((res: HttpResponse<ICourses[]>) => res.body ?? []))
      .subscribe((data: ICourses[]) => {
        this.coursesList = data;
        this.coursesOptions = this.coursesControl.valueChanges.pipe(
          startWith(''),
          map(res => this.coursesList.filter(item => String(item.courseName).toLowerCase().includes(String(res).toLowerCase())))
        );
      });
  }

  displayCourses(courses: any): any {
    return courses;
  }
  getStudentlist(): void {
    this.studentService
      .query({ sort: ['id'] })
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .subscribe((data: IStudent[]) => {
        this.studentList = data;
        this.studentOptions = this.studentControl.valueChanges.pipe(
          startWith(''),
          map(res => this.studentList.filter(item => String(item.firstName).toLowerCase().includes(String(res).toLowerCase())))
        );
      });
  }

  displayStudent(student: any): any {
    return student;
  }

  documentDownlound(): void {
    this.spivot.loginRequest();
  }

  saveValue(valinputstudy: any): void {
    sessionStorage.setItem('valinputstudy', JSON.stringify(valinputstudy));
  }

  filterAndSortEntities(page?: number, dontNavigate?: boolean): void {
    const datePattern = /^\d{4}-\d{2}-\d{2}$/;
    const pageToLoad: number = page ?? this.page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    for (const [key, value] of Object.entries(this.criteria)) {
      if (value) {
        if (typeof value === 'number') {
          queryObject[`${key}.equals`] = value.toString();
        } else if (typeof value === 'boolean' && value) {
          queryObject[`${key}.equals`] = value.toString();
        } else if (value.match(datePattern)) {
          queryObject[`${key}.equals`] = value;
        } else if (typeof value === 'string') {
          queryObject[`${key}.contains`] = value;
        }
      }
    }
    this.studyService.query(queryObject).subscribe(
      (res: HttpResponse<IStudy[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  clearFields(): void {
    this.criteria = {};
    this.getCourseslist();
    this.getStudentlist();
    this.loadPage();
    sessionStorage.setItem('valinputstudy', '');
  }

  trackId(index: number, item: IStudy): number {
    return item.id!;
  }

  delete(study: IStudy): void {
    const modalRef = this.modalService.open(StudyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.study = study;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IStudy[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/study'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.studies = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
