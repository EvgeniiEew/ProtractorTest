import { Component, OnInit, RendererFactory2, Renderer2, Output, EventEmitter } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { LoginService } from 'app/login/login.service';
import { SessionStorageService } from 'ngx-webstorage';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import * as dayjs from 'dayjs';
import { LANGUAGES } from 'app/config/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

interface Colloms {
  menuCol: string | undefined;
  mainCol: string | undefined;
}

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
  account: Account | null = null;
  colloms?: Colloms;
  languages = LANGUAGES;
  username?: string = this.account?.login;
  private renderer: Renderer2;

  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private loginService: LoginService,
    private sessionStorageService: SessionStorageService,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));

    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }

  parseCollomsObject(obj: Colloms): void {
    this.colloms = obj;
  }

  setMenuColloms(): string {
    return `card jh-card col-md-${this.colloms?.menuCol ?? '2'}`;
  }

  setMainColloms(): string {
    if (this.account !== null) {
      return `card jh-card col-md-${this.colloms?.mainCol ?? '10'}`;
    } else {
      return `card jh-card col-md-12`;
    }
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  changeLanguage(language: string): void {
    this.sessionStorageService.store('locale', language);
    this.translateService.use(language);
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    const title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      return this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }
}
