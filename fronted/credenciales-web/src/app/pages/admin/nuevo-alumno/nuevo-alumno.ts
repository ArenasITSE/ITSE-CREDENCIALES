import { Component, OnInit, inject } from '@angular/core';

import { CommonModule } from '@angular/common';

import { FormsModule } from '@angular/forms';

import { Router, RouterLink } from '@angular/router';

import Swal from 'sweetalert2';

import { AlumnoService } from '../../../core/services/alumno.service';

import { CarreraService } from '../../../core/services/carrera.service';

@Component({

  selector: 'app-nuevo-alumno',

  standalone: true,

  imports: [

    CommonModule,

    FormsModule,

    RouterLink

  ],

  templateUrl: './nuevo-alumno.html',

  styleUrl: './nuevo-alumno.css'

})

export class NuevoAlumno implements OnInit {

  private alumnoService = inject(AlumnoService);

  private carreraService = inject(CarreraService);

  private router = inject(Router);

  cargando = false;

  foto!: File;

  fotoPreview: string | null = null;

  carreras: any[] = [];

  alumno = {

    usuario: '',

    nombreCompleto: '',

    matricula: '',

    nss: '',

    carreraId: null,

    semestre: 1

  };

  ngOnInit(): void {

    this.cargarCarreras();

  }

  //=========================================
  // CARGAR CARRERAS
  //=========================================

  cargarCarreras(): void {

    this.carreraService.listar()

      .subscribe({

        next: (resp: any) => {

          this.carreras = resp;

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  //=========================================
  // SELECCIONAR FOTOGRAFÍA
  //=========================================

  seleccionarFoto(event: any): void {

    const archivo = event.target.files[0];

    if (!archivo) {

      return;

    }

    this.foto = archivo;

    const reader = new FileReader();

    reader.onload = () => {

      this.fotoPreview = reader.result as string;

    };

    reader.readAsDataURL(archivo);

  }

  //=========================================
  // LIMPIAR FORMULARIO
  //=========================================

  limpiarFormulario(): void {

    this.alumno = {

      usuario: '',

      nombreCompleto: '',

      matricula: '',

      nss: '',

      carreraId: null,

      semestre: 1

    };

    this.fotoPreview = null;

  }

  //=========================================
  // GUARDAR ALUMNO
  //=========================================

  guardarAlumno(): void {

    if (!this.alumno.usuario) {

      Swal.fire(
        'Usuario',
        'Ingrese el usuario.',
        'warning'
      );

      return;

    }

    if (!this.alumno.nombreCompleto) {

      Swal.fire(
        'Nombre',
        'Ingrese el nombre del alumno.',
        'warning'
      );

      return;

    }

    if (!this.alumno.matricula) {

      Swal.fire(
        'Matrícula',
        'Ingrese la matrícula.',
        'warning'
      );

      return;

    }

    //=========================================
    // VALIDAR NSS
    //=========================================

    if (!this.alumno.nss) {

      Swal.fire(
        'NSS',
        'Ingrese el NSS del alumno.',
        'warning'
      );

      return;

    }

    if (!/^\d{11}$/.test(this.alumno.nss)) {

      Swal.fire(
        'NSS',
        'El NSS debe contener exactamente 11 dígitos.',
        'warning'
      );

      return;

    }

    if (!this.alumno.carreraId) {

      Swal.fire(
        'Carrera',
        'Seleccione una carrera.',
        'warning'
      );

      return;

    }

    if (!this.foto) {

      Swal.fire(
        'Fotografía',
        'Seleccione una fotografía.',
        'warning'
      );

      return;

    }

    this.cargando = true;

    const formData = new FormData();

    formData.append(
      'usuario',
      this.alumno.usuario
    );

    formData.append(
      'nombreCompleto',
      this.alumno.nombreCompleto
    );

    formData.append(
      'matricula',
      this.alumno.matricula
    );

    formData.append(
      'nss',
      this.alumno.nss
    );

    formData.append(
      'carreraId',
      String(this.alumno.carreraId)
    );

    formData.append(
      'semestre',
      String(this.alumno.semestre)
    );

    formData.append(
      'foto',
      this.foto
    );

    this.alumnoService

      .registrarCompleto(formData)

      .subscribe({

        next: (resp: any) => {

          this.cargando = false;

          Swal.fire({

            icon: 'success',

            title: 'Alumno registrado',

            html: `

            <b>Usuario:</b> ${resp.usuario}<br><br>

            <b>Contraseña:</b> ${resp.passwordTemporal}<br><br>

            <b>Folio:</b> ${resp.folio}

            `,

            confirmButtonText: 'Aceptar'

          });

          this.limpiarFormulario();

        },

        error: (err) => {

          this.cargando = false;

          console.error(err);

          Swal.fire({

            icon: 'error',

            title: 'Error',

            text:
              err.error?.message ??
              'No fue posible registrar al alumno.'

          });

        }

      });

  }

  //=========================================
  // REGRESAR
  //=========================================

  regresar(): void {

    this.router.navigate([

      '/dashboard'

    ]);

  }

  semestres = [1,2,3,4,5,6,7,8,9,];

  obtenerNombreCarrera(): string {

    const carrera = this.carreras.find(

      c => c.id == this.alumno.carreraId

    );

    return carrera ? carrera.nombre : 'Sin seleccionar';

  }

}