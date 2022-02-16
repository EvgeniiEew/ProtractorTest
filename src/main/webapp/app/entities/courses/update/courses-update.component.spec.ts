jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CoursesService } from '../service/courses.service';
import { ICourses, Courses } from '../courses.model';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';

import { CoursesUpdateComponent } from './courses-update.component';

describe('Courses Management Update Component', () => {
  let comp: CoursesUpdateComponent;
  let fixture: ComponentFixture<CoursesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let coursesService: CoursesService;
  let providerService: ProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CoursesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CoursesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CoursesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    coursesService = TestBed.inject(CoursesService);
    providerService = TestBed.inject(ProviderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Provider query and add missing value', () => {
      const courses: ICourses = { id: 456 };
      const name: IProvider = { id: 88841 };
      courses.name = name;

      const providerCollection: IProvider[] = [{ id: 87279 }];
      jest.spyOn(providerService, 'query').mockReturnValue(of(new HttpResponse({ body: providerCollection })));
      const additionalProviders = [name];
      const expectedCollection: IProvider[] = [...additionalProviders, ...providerCollection];
      jest.spyOn(providerService, 'addProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ courses });
      comp.ngOnInit();

      expect(providerService.query).toHaveBeenCalled();
      expect(providerService.addProviderToCollectionIfMissing).toHaveBeenCalledWith(providerCollection, ...additionalProviders);
      expect(comp.providersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const courses: ICourses = { id: 456 };
      const name: IProvider = { id: 82671 };
      courses.name = name;

      activatedRoute.data = of({ courses });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(courses));
      expect(comp.providersSharedCollection).toContain(name);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Courses>>();
      const courses = { id: 123 };
      jest.spyOn(coursesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: courses }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(coursesService.update).toHaveBeenCalledWith(courses);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Courses>>();
      const courses = new Courses();
      jest.spyOn(coursesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: courses }));
      saveSubject.complete();

      // THEN
      expect(coursesService.create).toHaveBeenCalledWith(courses);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Courses>>();
      const courses = { id: 123 };
      jest.spyOn(coursesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(coursesService.update).toHaveBeenCalledWith(courses);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProviderById', () => {
      it('Should return tracked Provider primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProviderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
