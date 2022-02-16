import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map, startWith } from 'rxjs/operators';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';

import { ICourses, Courses } from '../courses.model';
import { CoursesService } from '../service/courses.service';
import { IProvider } from 'app/entities/provider/provider.model';
import { ProviderService } from 'app/entities/provider/service/provider.service';

@Component({
  selector: 'jhi-courses-update',
  templateUrl: './courses-update.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class CoursesUpdateComponent implements OnInit {
  isSaving = false;
  providersCurrentPage = 1;
  providersSharedCollection: IProvider[] = [];
  providersList: IProvider[] = [];
  providersfilteredOptions?: Observable<IProvider[]>;

  editForm = this.fb.group({
    id: [],
    courseName: [null, [Validators.required]],
    dateOfStart: [null, [Validators.required]],
    dateOfEnd: [null, [Validators.required]],
    name: [],
  });

  constructor(
    protected coursesService: CoursesService,
    protected providerService: ProviderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courses }) => {
      this.updateForm(courses);

      this.loadRelationshipsOptions();
    });
    this.getProviderlist();
  }

  displayname(subject: any): any {
    return `Provider - ${String(subject.name)}`;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courses = this.createFromForm();
    if (courses.id !== undefined) {
      this.subscribeToSaveResponse(this.coursesService.update(courses));
    } else {
      this.subscribeToSaveResponse(this.coursesService.create(courses));
    }
  }

  trackProviderById(index: number, item: IProvider): number {
    return item.id!;
  }

  onScrollProvider(): void {
    this.providerService
      .query({ page: this.providersCurrentPage, size: ITEMS_PER_PAGE, sort: ['id'] })
      .subscribe((res: HttpResponse<IProvider[]>) => {
        this.providersSharedCollection = this.providersSharedCollection.concat(res.body ?? []);
      });
    this.providersCurrentPage++;
  }

  getProviderlist(): void {
    this.providerService
      .query({ sort: ['id'] })
      .pipe(map((res: HttpResponse<IProvider[]>) => res.body ?? []))
      .subscribe((data: IProvider[]) => {
        this.providersList = data;
        this.providersfilteredOptions = this.editForm.get('name')?.valueChanges.pipe(
          startWith(''),
          map(res => this.providersList.filter(item => String(item.name).toLowerCase().includes(String(res).toLowerCase())))
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourses>>): void {
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

  protected updateForm(courses: ICourses): void {
    this.editForm.patchValue({
      id: courses.id,
      courseName: courses.courseName,
      dateOfStart: courses.dateOfStart,
      dateOfEnd: courses.dateOfEnd,
      name: courses.name,
    });

    this.providersSharedCollection = this.providerService.addProviderToCollectionIfMissing(this.providersSharedCollection, courses.name);
  }

  protected loadRelationshipsOptions(): void {
    this.providerService
      .query({ sort: ['id'] })
      .pipe(map((res: HttpResponse<IProvider[]>) => res.body ?? []))
      .pipe(
        map((providers: IProvider[]) => this.providerService.addProviderToCollectionIfMissing(providers, this.editForm.get('name')!.value))
      )
      .subscribe((providers: IProvider[]) => (this.providersSharedCollection = providers));
  }

  protected createFromForm(): ICourses {
    return {
      ...new Courses(),
      id: this.editForm.get(['id'])!.value,
      courseName: this.editForm.get(['courseName'])!.value,
      dateOfStart: this.editForm.get(['dateOfStart'])!.value,
      dateOfEnd: this.editForm.get(['dateOfEnd'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
