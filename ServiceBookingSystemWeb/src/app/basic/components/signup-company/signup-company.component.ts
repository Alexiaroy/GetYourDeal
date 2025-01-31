import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-signup-company',
  standalone: false,

  templateUrl: './signup-company.component.html',
  styleUrl: '../signup-client/signup-client.component.css',
})
export class SignupCompanyComponent {
  validateForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private notification: NzNotificationService,
    private router: Router
  ) {}

  ngOnInit() {
    this.validateForm = this.fb.group({
      email: [null, [Validators.email, Validators.required]],
      name: [null, [Validators.required]],
      address: [null, [Validators.required]],
      phone: [null],
      password: [null, [Validators.required]],
      checkPassword: [null, [Validators.required]],
    });
  }

  submitForm() {
    if (this.validateForm.valid) {
      this.authService.registerCompany(this.validateForm.value).subscribe({
        next: (res) => {
          // En cas de succès
          this.notification.success('SUCCESS', `Signup successful`, {
            nzDuration: 5000,
          });
          this.router.navigateByUrl('/login');
        },
        error: (error) => {
          // En cas d'erreur
          this.notification.error('ERROR', `${error.error}`, {
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
