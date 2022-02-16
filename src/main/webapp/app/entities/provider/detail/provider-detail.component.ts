import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { HttpResponse } from '@angular/common/http';

import { UserService } from '../../user/user.service';
import { IProvider } from '../provider.model';

@Component({
  selector: 'jhi-provider-detail',
  templateUrl: './provider-detail.component.html',
  styleUrls: ['../../global-entities-style.scss'],
})
export class ProviderDetailComponent implements OnInit {
  provider: IProvider | null = null;

  adminsAccess: any = false;

  constructor(protected activatedRoute: ActivatedRoute, protected userService: UserService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ provider }) => {
      this.provider = provider;
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
