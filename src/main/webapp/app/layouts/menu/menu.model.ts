export interface Entities {
  name: string;
  path: string;
  jhiTranslateKey: string;
  langCode: string;
  group: string;
}

export interface MenuGroup {
  name: string;
  role: string | null;
  langCode: string;
  path: string;
  jhiTranslateKey: string;
  open: boolean;
  entities: Entities[];
}

export interface MainMenu {
  name: string;
  role: string | null;
  jhiTranslateKey: string;
  menuGroup: MenuGroup[];
}
