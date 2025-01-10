import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SkimaValidators } from '../../../_shared/validators/skima-validators';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../_shared/auth/auth.service';
import { StatusApi } from '../../../api/services/status-api.service';
import { SkimaView } from '../../../_shared/structure/skima-view';
import { HttpErrorResponse } from '@angular/common/http';
import { MatCard, MatCardContent } from '@angular/material/card';
import { MatFormField, MatFormFieldModule } from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { MatTooltip } from '@angular/material/tooltip';
import { SkimaNavigator } from '../../../_shared/services/skima-navigator.service';

@Component({
  selector: 'skima-login-view',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCard,
    MatCardContent,
    MatFormField,
    MatInput,
    MatButton,
    MatFormFieldModule,
    MatInputModule,
    MatTooltip,
  ],
  templateUrl: './login-view.component.html',
  styleUrl: './login-view.component.scss',
})
export class LoginView extends SkimaView implements OnInit {
  loginForm = new FormGroup({
    username: new FormControl('', SkimaValidators.notEmpty),
    password: new FormControl('', SkimaValidators.notEmpty),
  });

  private returnUrl = '';
  errorMsg: string | null = null;
  version = '';

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private statusApi: StatusApi,
    skimaNavigator: SkimaNavigator,
  ) {
    super(skimaNavigator);

    this.statusApi.getStatus().subscribe((status) => {
      this.version = status.version;
    });
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || this.returnUrl;
  }

  login(): void {
    this.loginForm.markAllAsTouched();
    this.loginForm.updateValueAndValidity();
    console.log(this.loginForm.value);
    if (!this.loginForm.valid) return;

    const value = this.loginForm.value;
    this.authService.login(value.username!, value.password!).subscribe({
      next: () => this.router.navigateByUrl(this.returnUrl),
      error: (err: any) => this.onLoginError(err),
    });
  }

  private onLoginError(error: any) {
    if (error instanceof HttpErrorResponse && error.status === 401) {
      this.errorMsg = 'Falsche Login-Daten!';
    }
  }
}
