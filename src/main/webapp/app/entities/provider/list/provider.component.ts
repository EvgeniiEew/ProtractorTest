import { IProviderCriteria } from '../provider.criteria';
import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { UserService } from '../../user/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { FormControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProvider } from '../provider.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { ProviderService } from '../service/provider.service';
import { ProviderDeleteDialogComponent } from '../delete/provider-delete-dialog.component';
import { Spivot } from '../spivot/spivot';

@Component({
  selector: 'jhi-provider',
  templateUrl: './provider.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class ProviderComponent implements OnInit {
  providers?: IProvider[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  visibleCreateButton = false;
  adminsAccess: any = false;
  criteria: IProviderCriteria;
  sesionValue: any;

  constructor(
    protected spivot: Spivot,
    protected providerService: ProviderService,
    protected activatedRoute: ActivatedRoute,
    protected userService: UserService,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    this.criteria = {};
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.providerService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IProvider[]>) => {
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

    this.sesionValue = sessionStorage.getItem('valinputprovider');
    if (this.sesionValue != null) {
      this.criteria = JSON.parse(this.sesionValue);
      this.filterAndSortEntities(1);
    }
  }

  documentDownlound(): void {
    this.spivot.loginRequest();
  }

  saveValue(valinputprovider: any): void {
    sessionStorage.setItem('valinputprovider', JSON.stringify(valinputprovider));
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
    this.providerService.query(queryObject).subscribe(
      (res: HttpResponse<IProvider[]>) => {
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
    this.loadPage();
    sessionStorage.setItem('valinputprovider', '');
  }

  trackId(index: number, item: IProvider): number {
    return item.id!;
  }

  delete(provider: IProvider): void {
    const modalRef = this.modalService.open(ProviderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.provider = provider;
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

  protected onSuccess(data: IProvider[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/provider'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.providers = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
