import {browser, element, by} from 'protractor';

import { NavBarPage } from '../page-objects/jhi-page-objects';

describe('account', () => {
  let navBarPage: NavBarPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';
  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage(true);
  });


  it('should login successfully with admin account', async () => {
    await browser.sleep(1000);
    let input_login =element(by.css("input[id='username']"));
    await input_login.sendKeys(username);
    await browser.sleep(1000);
    let input_password =element(by.css("input[formcontrolname='password']"));
    await browser.sleep(1000);
    await input_password.sendKeys(password);
    let submit_button = element(by.id('loginButton'));
    await browser.sleep(1000);
    await submit_button.click();
  });
});
