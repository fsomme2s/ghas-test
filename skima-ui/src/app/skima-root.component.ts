import en from '../../public/i18n/en.json';

import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { StatusDto } from './api/model/api.types';
import { StatusApi } from './api/services/status-api.service';
import { AuthService } from './_shared/auth/auth.service';
import { MatButton } from '@angular/material/button';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'skima-root',
  standalone: true,
  imports: [RouterOutlet, MatButton, RouterLink],
  templateUrl: './skima-root.component.html',
  styleUrl: './skima-root.component.scss',
})
export class SkimaRootComponent {
  title = 'skima-ui';
  statusDto?: StatusDto;
  loggedIn = false;

  constructor(
    private translateService: TranslateService,
    private statusApi: StatusApi,
    private authService: AuthService
  ) {
    this.translateService.addLangs(['en']);
    this.translateService.setTranslation('en', en);
    this.translateService.setDefaultLang('en');

    this.statusApi.getStatus().subscribe((statusDto: StatusDto) => (this.statusDto = statusDto));

    // Initial State (= on page reload with a still valid token in cache/localStorage):
    // if (authService.isLoggedIn()) {
    //   this.fetchCurrentUser() todo
    this.loggedIn = authService.isLoggedIn();
    // }

    // Subscribe to changes:
    authService.onLogin.subscribe((loggedIn) => {
      this.loggedIn = loggedIn;
      // if (loggedIn) { todo
      //   this.fetchCurrentUser()
      // } else {
      //   this.currentUser.next(null) // user logged out = no current user
      // }
    });
  }

  logout() {
    this.authService.logout();
  }

  testError(type: 'UNEXPECTED' | 'USER') {
    this.statusApi.provokeError(type).subscribe();
  }
}
