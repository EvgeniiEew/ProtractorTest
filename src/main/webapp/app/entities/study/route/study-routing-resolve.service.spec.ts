jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStudy, Study } from '../study.model';
import { StudyService } from '../service/study.service';

import { StudyRoutingResolveService } from './study-routing-resolve.service';

describe('Study routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: StudyRoutingResolveService;
  let service: StudyService;
  let resultStudy: IStudy | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(StudyRoutingResolveService);
    service = TestBed.inject(StudyService);
    resultStudy = undefined;
  });

  describe('resolve', () => {
    it('should return IStudy returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStudy = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStudy).toEqual({ id: 123 });
    });

    it('should return new IStudy if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStudy = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultStudy).toEqual(new Study());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Study })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStudy = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStudy).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
