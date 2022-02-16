import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map, startWith } from 'rxjs/operators';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';

import { IStudy, Study } from '../study.model';
import { StudyService } from '../service/study.service';
import { ICourses } from 'app/entities/courses/courses.model';
import { CoursesService } from 'app/entities/courses/service/courses.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

@Component({
  selector: 'jhi-study-update',
  templateUrl: './study-update.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class StudyUpdateComponent implements OnInit {
  isSaving = false;
  coursesCurrentPage = 1;
  coursesSharedCollection: ICourses[] = [];
  coursesList: ICourses[] = [];
  coursesfilteredOptions?: Observable<ICourses[]>;

  studentsCurrentPage = 1;
  studentsSharedCollection: IStudent[] = [];
  studentsList: IStudent[] = [];
  studentsfilteredOptions?: Observable<IStudent[]>;

  editForm = this.fb.group({
    id: [],
    dateOfStart: [null, [Validators.required]],
    dateOfExam: [null, [Validators.required]],
    grade: [null, [Validators.required]],
    coursename: [],
    student: [],
  });

  constructor(
    protected studyService: StudyService,
    protected coursesService: CoursesService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ study }) => {
      this.updateForm(study);

      this.loadRelationshipsOptions();
    });
    this.getCourseslist();
    this.getStudentlist();
  }

  displaycoursename(subject: any): any {
    return subject ? subject.courseName : undefined;
  }
  displaystudent(subject: any): any {
    return `Name - ${String(subject.firstName)}`;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const study = this.createFromForm();
    if (study.id !== undefined) {
      this.subscribeToSaveResponse(this.studyService.update(study));
    } else {
      this.subscribeToSaveResponse(this.studyService.create(study));
    }
  }

  trackCoursesById(index: number, item: ICourses): number {
    return item.id!;
  }

  trackStudentById(index: number, item: IStudent): number {
    return item.id!;
  }

  onScrollCourses(): void {
    this.coursesService
      .query({ page: this.coursesCurrentPage, size: ITEMS_PER_PAGE, sort: ['id'] })
      .subscribe((res: HttpResponse<ICourses[]>) => {
        this.coursesSharedCollection = this.coursesSharedCollection.concat(res.body ?? []);
      });
    this.coursesCurrentPage++;
  }

  getCourseslist(): void {
    this.coursesService
      .query({ sort: ['id'] })
      .pipe(map((res: HttpResponse<ICourses[]>) => res.body ?? []))
      .subscribe((data: ICourses[]) => {
        this.coursesList = data;
        this.coursesfilteredOptions = this.editForm.get('coursename')?.valueChanges.pipe(
          startWith(''),
          map(res => this.coursesList.filter(item => String(item.courseName).toLowerCase().includes(String(res).toLowerCase())))
        );
      });
  }

  onScrollStudent(): void {
    this.studentService
      .query({ page: this.studentsCurrentPage, size: ITEMS_PER_PAGE, sort: ['id'] })
      .subscribe((res: HttpResponse<IStudent[]>) => {
        this.studentsSharedCollection = this.studentsSharedCollection.concat(res.body ?? []);
      });
    this.studentsCurrentPage++;
  }

  getStudentlist(): void {
    this.studentService
      .query({ sort: ['id'] })
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .subscribe((data: IStudent[]) => {
        this.studentsList = data;
        this.studentsfilteredOptions = this.editForm.get('student')?.valueChanges.pipe(
          startWith(''),
          map(res => this.studentsList.filter(item => String(item.firstName).toLowerCase().includes(String(res).toLowerCase())))
        );
      });
  }

  setCollomsFromWidth(): string {
    const windowInnerWidth = window.innerWidth;
    if (windowInnerWidth < 480) {
      return 'col-12 ';
    } else {
      return 'col-8';
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudy>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(study: IStudy): void {
    this.editForm.patchValue({
      id: study.id,
      dateOfStart: study.dateOfStart,
      dateOfExam: study.dateOfExam,
      grade: study.grade,
      coursename: study.coursename,
      student: study.student,
    });

    this.coursesSharedCollection = this.coursesService.addCoursesToCollectionIfMissing(this.coursesSharedCollection, study.coursename);
    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing(this.studentsSharedCollection, study.student);
  }

  protected loadRelationshipsOptions(): void {
    this.coursesService
      .query({ sort: ['id'] })
      .pipe(map((res: HttpResponse<ICourses[]>) => res.body ?? []))
      .pipe(
        map((courses: ICourses[]) => this.coursesService.addCoursesToCollectionIfMissing(courses, this.editForm.get('coursename')!.value))
      )
      .subscribe((courses: ICourses[]) => (this.coursesSharedCollection = courses));

    this.studentService
      .query({ sort: ['id'] })
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) => this.studentService.addStudentToCollectionIfMissing(students, this.editForm.get('student')!.value))
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));
  }

  protected createFromForm(): IStudy {
    return {
      ...new Study(),
      id: this.editForm.get(['id'])!.value,
      dateOfStart: this.editForm.get(['dateOfStart'])!.value,
      dateOfExam: this.editForm.get(['dateOfExam'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      coursename: this.editForm.get(['coursename'])!.value,
      student: this.editForm.get(['student'])!.value,
    };
  }
}
