import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { AuthService } from '../../services/auth/auth.service';
import { UserStorageService } from '../../services/storage/user-storage.service';

@Component({
  selector: 'app-login',
  standalone: false,

  templateUrl: './login.component.html',
  styleUrl: '../signup-client/signup-client.component.css',
})
export class LoginComponent {
  validateForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private notification: NzNotificationService,
    private router: Router
  ) {}

  ngOnInit() {
    this.validateForm = this.fb.group({
      username: [null, [Validators.email, Validators.required]],
      password: [null, [Validators.required]],
    });
  }

  submitForm() {
    if (this.validateForm.valid) {
      const username = this.validateForm.get('username')?.value;
      const password = this.validateForm.get('password')?.value;

      this.authService.login(username, password).subscribe({
        next: (res) => {
          // En cas de succès
          if (UserStorageService.isClientLoggedIn()) {
            this.router.navigateByUrl('client/dashboard');
          } else if (UserStorageService.isCompanyLoggedIn()) {
            this.router.navigateByUrl('company/dashboard');
          }
        },
        error: (error) => {
          // En cas d'erreur
          this.notification.error('ERROR', `Email ou mot de passe incorrect`, {
            nzDuration: 5000,
          });
        },
      });
    } else {
      Object.values(this.validateForm.controls).forEach((control) => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity();
        }
      });
      this.notification.error(
        'ERROR',
        'Formulaire invalide, veuillez corriger les erreurs',
        {
          nzDuration: 5000,
        }
      );
    }
  }
}
