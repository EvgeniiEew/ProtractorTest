import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IStudy, Study } from '../study.model';

import { StudyService } from './study.service';

describe('Study Service', () => {
  let service: StudyService;
  let httpMock: HttpTestingController;
  let elemDefault: IStudy;
  let expectedResult: IStudy | IStudy[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StudyService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dateOfStart: currentDate,
      dateOfExam: currentDate,
      grade: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateOfStart: currentDate.format(DATE_FORMAT),
          dateOfExam: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Study', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateOfStart: currentDate.format(DATE_FORMAT),
          dateOfExam: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOfStart: currentDate,
          dateOfExam: currentDate,
        },
        returnedFromService
      );

      service.create(new Study()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Study', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateOfStart: currentDate.format(DATE_FORMAT),
          dateOfExam: currentDate.format(DATE_FORMAT),
          grade: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOfStart: currentDate,
          dateOfExam: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Study', () => {
      const patchObject = Object.assign({}, new Study());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateOfStart: currentDate,
          dateOfExam: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Study', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateOfStart: currentDate.format(DATE_FORMAT),
          dateOfExam: currentDate.format(DATE_FORMAT),
          grade: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOfStart: currentDate,
          dateOfExam: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Study', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStudyToCollectionIfMissing', () => {
      it('should add a Study to an empty array', () => {
        const study: IStudy = { id: 123 };
        expectedResult = service.addStudyToCollectionIfMissing([], study);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(study);
      });

      it('should not add a Study to an array that contains it', () => {
        const study: IStudy = { id: 123 };
        const studyCollection: IStudy[] = [
          {
            ...study,
          },
          { id: 456 },
        ];
        expectedResult = service.addStudyToCollectionIfMissing(studyCollection, study);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Study to an array that doesn't contain it", () => {
        const study: IStudy = { id: 123 };
        const studyCollection: IStudy[] = [{ id: 456 }];
        expectedResult = service.addStudyToCollectionIfMissing(studyCollection, study);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(study);
      });

      it('should add only unique Study to an array', () => {
        const studyArray: IStudy[] = [{ id: 123 }, { id: 456 }, { id: 89637 }];
        const studyCollection: IStudy[] = [{ id: 123 }];
        expectedResult = service.addStudyToCollectionIfMissing(studyCollection, ...studyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const study: IStudy = { id: 123 };
        const study2: IStudy = { id: 456 };
        expectedResult = service.addStudyToCollectionIfMissing([], study, study2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(study);
        expect(expectedResult).toContain(study2);
      });

      it('should accept null and undefined values', () => {
        const study: IStudy = { id: 123 };
        expectedResult = service.addStudyToCollectionIfMissing([], null, study, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(study);
      });

      it('should return initial array if no Study is added', () => {
        const studyCollection: IStudy[] = [{ id: 123 }];
        expectedResult = service.addStudyToCollectionIfMissing(studyCollection, undefined, null);
        expect(expectedResult).toEqual(studyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
