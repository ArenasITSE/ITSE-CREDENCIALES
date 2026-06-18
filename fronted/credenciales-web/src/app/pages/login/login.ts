import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';

import Swal from 'sweetalert2';

import { AuthService } from '../../core/services/auth.service';
import { StorageService } from '../../core/services/storage.service';
import { LoginRequest } from '../../core/models/login-request';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private storage = inject(StorageService);
  private router = inject(Router);

  mostrarPassword = false;

  cargando = false;

  loginForm = this.fb.nonNullable.group({

    username: [
      '',
      Validators.required
    ],

    password: [
      '',
      Validators.required
    ]

  });

  iniciarSesion(): void {

    if (this.loginForm.invalid) {

      this.loginForm.markAllAsTouched();

      return;

    }

    this.cargando = true;

    const login: LoginRequest = this.loginForm.getRawValue();

    this.authService.login(login)
      .subscribe({

        next: (resp) => {

          // ============================
          // Guardar Token
          // ============================

          this.storage.guardarToken(resp.token);

          // ============================
          // Obtener Rol
          // ============================

          const rol = this.storage.obtenerRol();

          Swal.fire({

            icon: 'success',
            title: 'Bienvenido',
            text: 'Inicio de sesión correcto',
            timer: 1500,
            showConfirmButton: false

          }).then(() => {

            if (rol === 'ALUMNO') {

              this.router.navigate([
                '/alumnos'
              ]);

            } else {

              this.router.navigate([
                '/dashboard'
              ]);

            }

          });

        },

        error: (error) => {

          console.error(error);

          this.cargando = false;

          Swal.fire({

            icon: 'error',
            title: 'Error',
            text: 'Usuario o contraseña incorrectos'

          });

        },

        complete: () => {

          this.cargando = false;

        }

      });

  }

}