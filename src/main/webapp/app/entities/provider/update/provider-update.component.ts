import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';

import { IProvider, Provider } from '../provider.model';
import { ProviderService } from '../service/provider.service';

@Component({
  selector: 'jhi-provider-update',
  templateUrl: './provider-update.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class ProviderUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(25)]],
  });

  constructor(protected providerService: ProviderService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ provider }) => {
      this.updateForm(provider);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const provider = this.createFromForm();
    if (provider.id !== undefined) {
      this.subscribeToSaveResponse(this.providerService.update(provider));
    } else {
      this.subscribeToSaveResponse(this.providerService.create(provider));
    }
  }

  setCollomsFromWidth(): string {
    const windowInnerWidth = window.innerWidth;
    if (windowInnerWidth < 480) {
      return 'col-12 ';
    } else {
      return 'col-8';
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvider>>): void {
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

  protected updateForm(provider: IProvider): void {
    this.editForm.patchValue({
      id: provider.id,
      name: provider.name,
    });
  }

  protected createFromForm(): IProvider {
    return {
      ...new Provider(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
