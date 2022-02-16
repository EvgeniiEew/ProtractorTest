jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudyService } from '../service/study.service';
import { IStudy, Study } from '../study.model';
import { ICourses } from 'app/entities/courses/courses.model';
import { CoursesService } from 'app/entities/courses/service/courses.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

import { StudyUpdateComponent } from './study-update.component';

describe('Study Management Update Component', () => {
  let comp: StudyUpdateComponent;
  let fixture: ComponentFixture<StudyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let studyService: StudyService;
  let coursesService: CoursesService;
  let studentService: StudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StudyUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(StudyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    studyService = TestBed.inject(StudyService);
    coursesService = TestBed.inject(CoursesService);
    studentService = TestBed.inject(StudentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Courses query and add missing value', () => {
      const study: IStudy = { id: 456 };
      const coursename: ICourses = { id: 28179 };
      study.coursename = coursename;

      const coursesCollection: ICourses[] = [{ id: 78108 }];
      jest.spyOn(coursesService, 'query').mockReturnValue(of(new HttpResponse({ body: coursesCollection })));
      const additionalCourses = [coursename];
      const expectedCollection: ICourses[] = [...additionalCourses, ...coursesCollection];
      jest.spyOn(coursesService, 'addCoursesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ study });
      comp.ngOnInit();

      expect(coursesService.query).toHaveBeenCalled();
      expect(coursesService.addCoursesToCollectionIfMissing).toHaveBeenCalledWith(coursesCollection, ...additionalCourses);
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Student query and add missing value', () => {
      const study: IStudy = { id: 456 };
      const student: IStudent = { id: 23405 };
      study.student = student;

      const studentCollection: IStudent[] = [{ id: 56648 }];
      jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
      const additionalStudents = [student];
      const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
      jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ study });
      comp.ngOnInit();

      expect(studentService.query).toHaveBeenCalled();
      expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, ...additionalStudents);
      expect(comp.studentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const study: IStudy = { id: 456 };
      const coursename: ICourses = { id: 4765 };
      study.coursename = coursename;
      const student: IStudent = { id: 32004 };
      study.student = student;

      activatedRoute.data = of({ study });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(study));
      expect(comp.coursesSharedCollection).toContain(coursename);
      expect(comp.studentsSharedCollection).toContain(student);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Study>>();
      const study = { id: 123 };
      jest.spyOn(studyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ study });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: study }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(studyService.update).toHaveBeenCalledWith(study);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Study>>();
      const study = new Study();
      jest.spyOn(studyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ study });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: study }));
      saveSubject.complete();

      // THEN
      expect(studyService.create).toHaveBeenCalledWith(study);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Study>>();
      const study = { id: 123 };
      jest.spyOn(studyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ study });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(studyService.update).toHaveBeenCalledWith(study);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCoursesById', () => {
      it('Should return tracked Courses primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCoursesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackStudentById', () => {
      it('Should return tracked Student primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStudentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
