import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { Entities, MainMenu, MenuGroup } from './menu.model';

import { LANGUAGES } from 'app/config/language.constants';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';
import { NgbAlertConfig } from '@ng-bootstrap/ng-bootstrap'; // for animation

import { LoginService } from 'app/login/login.service';

interface Colloms {
  menuCol: string;
  mainCol: string;
}

@Component({
  selector: 'jhi-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
})
export class MenuComponent implements OnInit {
  @Output() newCollomsEvent = new EventEmitter<Colloms>();
  @Input() toggle?: boolean;
  account: Account | null = null;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  isCollapsed = false;
  toogleMenuGroup = false;
  groupName = '';

  mainMenu: MainMenu[] = [
    {
      name: 'Data',
      jhiTranslateKey: 'dataDropDownMenu.entitiesName',
      role: 'ROLE_USER',
      menuGroup: [
        {
          name: 'Available courses',
          jhiTranslateKey: 'dataDropDownMenu.entitiesName',
          role: 'ROLE_USER',
          langCode: '',
          path: '',
          open: false,
          entities: [
            {
              name: 'Courses',
              path: 'courses',
              jhiTranslateKey: '',
              langCode: '',
              group: 'Available courses',
            },
          ],
        },

        {
          name: 'Working data',
          jhiTranslateKey: 'dataDropDownMenu.entitiesName',
          role: 'ROLE_USER',
          langCode: '',
          path: '',
          open: false,
          entities: [
            {
              name: 'Providers',
              path: 'provider',
              jhiTranslateKey: '',
              langCode: '',
              group: 'Working data',
            },
            {
              name: 'Students',
              path: 'student',
              jhiTranslateKey: '',
              langCode: '',
              group: 'Working data',
            },
            {
              name: 'Studies',
              path: 'study',
              jhiTranslateKey: '',
              langCode: '',
              group: 'Working data',
            },
          ],
        },
      ],
    },
    {
      name: 'Administration',
      jhiTranslateKey: 'dataDropDownMenu.administration',
      role: 'ROLE_ADMIN',
      menuGroup: [
        {
          name: 'User management',
          path: 'admin/user-management',
          jhiTranslateKey: 'global.menu.admin.userManagement',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'Metrics',
          path: 'admin/metrics',
          jhiTranslateKey: 'global.menu.admin.metrics',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'Health',
          path: 'admin/health',
          jhiTranslateKey: 'global.menu.admin.health',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'Configuration',
          path: 'admin/configuration',
          jhiTranslateKey: 'global.menu.admin.configuration',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'Logs',
          path: 'admin/logs',
          jhiTranslateKey: 'global.menu.admin.logs',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'API',
          path: 'admin/docs',
          jhiTranslateKey: 'global.menu.admin.apidocs',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
      ],
    },
    {
      name: 'Links',
      role: 'ROLE_USER',
      jhiTranslateKey: 'dataDropDownMenu.links',
      menuGroup: [
        {
          name: 'Home',
          path: '/',
          jhiTranslateKey: '',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'Acl',
          path: 'https://tiger.sqilsoft.by/acl/dev',
          jhiTranslateKey: '',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'Car',
          path: 'https://tiger.sqilsoft.by/car/dev',
          jhiTranslateKey: '',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'Library',
          path: 'https://tiger.sqilsoft.by/library/dev',
          jhiTranslateKey: '',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
      ],
    },
    {
      name: 'Language',
      role: 'ROLE_USER',
      jhiTranslateKey: 'dataDropDownMenu.language',
      menuGroup: [
        {
          name: '',
          path: '',
          jhiTranslateKey: '',
          langCode: 'en',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: '',
          path: '',
          jhiTranslateKey: '',
          langCode: 'de',
          role: '',
          open: false,
          entities: [],
        },
      ],
    },
    {
      name: 'Account',
      jhiTranslateKey: 'global.menu.account.main',
      role: 'ROLE_USER',
      menuGroup: [
        {
          name: 'Settings',
          path: 'account/settings',
          jhiTranslateKey: 'global.menu.account.settings',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'Password',
          path: 'account/password',
          jhiTranslateKey: 'global.menu.account.password',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
        {
          name: 'Sign out',
          path: '',
          jhiTranslateKey: 'global.menu.account.logout',
          langCode: '',
          role: '',
          open: false,
          entities: [],
        },
      ],
    },
  ];

  constructor(
    ngbAlertConfig: NgbAlertConfig, // animation (disable)
    private accountService: AccountService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private loginService: LoginService,
    private router: Router,
  ) {
    ngbAlertConfig.animation = false;
  }

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.setInitialMenuState();
  }

  pathToEntity(): string {
    return '/';
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  changeColloms(): void {
    this.newCollomsEvent.emit(this.setColloms());
  }

  setColloms(): Colloms {
    if (this.isCollapsed && this.account !== null) {
      return {
        menuCol: '1half',
        mainCol: '11half',
      };
    } else {
      return {
        menuCol: '2',
        mainCol: '10',
      };
    }
  }

  getSortedEntities(itemEntities: Entities[]): Entities[] {
    return itemEntities.sort((value, nextValue) => (value.name > nextValue.name ? 1 : -1));
  }

  getSortedMenuGroup(itemGroup: MenuGroup[]): MenuGroup[] {
    return itemGroup.sort((value, nextValue) => (value.name > nextValue.name ? 1 : -1));
  }

  setCollapseClickEvent(collapse: any): void {
    const windowInnerWidth = window.innerWidth;
    if (windowInnerWidth < 480) {
      collapse.toggle();
    }
  }

  setInitialMenuState(): void {
    const windowInnerWidth = window.innerWidth;
    if (windowInnerWidth < 480) {
      this.isCollapsed = true;
    }
  }

  toggleToCollapseOneGroup(groupName: string): void {
    this.mainMenu
      .find(menu => menu.name === 'Data')
      ?.menuGroup.map(group => {
        if (group.name === groupName) {
          group.open = !group.open;
        }
      });
  }

  toggleCollapseAllGroups(): void {
    this.mainMenu
      .find(menu => menu.name === 'Data')
      ?.menuGroup.forEach(group => {
        group.open = false;
      });
  }

  collapseMenuGroup(): string {
    if (this.mainMenu[0].menuGroup[0].open) {
      return `inner-accordion-item-body collapseMenuGroupEntity`;
    } else {
      return `inner-accordion-item-body menuGroupEntity`;
    }
  }
}
