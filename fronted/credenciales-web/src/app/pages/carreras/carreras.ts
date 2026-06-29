import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import Swal from 'sweetalert2';

import { CarreraService } from '../../core/services/carrera.service';
import { Carrera } from '../../core/models/carrera';
import { DashboardCarrera } from '../../core/models/dashboard-carrera';

@Component({
  selector: 'app-carreras',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './carreras.html',
  styleUrls: ['./carreras.css']
})
export class Carreras implements OnInit {

  private carreraService = inject(CarreraService);

  //==========================================
  // LISTA
  //==========================================

  carreras: Carrera[] = [];

  carrerasOriginal: Carrera[] = [];

  //==========================================
  // DASHBOARD
  //==========================================

  dashboard!: DashboardCarrera;

  //==========================================
  // BUSCADOR
  //==========================================

  textoBusqueda = '';

  //==========================================
  // MODAL
  //==========================================

  mostrarModal = false;

  editando = false;

  //==========================================
  // FORMULARIO
  //==========================================

carrera: Carrera = {

    nombre:'',

    abreviatura:'',

    totalAlumnos:0

} as Carrera;
  //==========================================
  // INIT
  //==========================================

  ngOnInit(): void {

    this.cargarDashboard();

    this.cargarCarreras();

  }

    //==========================================
  // CARGAR DASHBOARD
  //==========================================

  cargarDashboard(): void {

    this.carreraService

      .dashboard()

      .subscribe({

        next: (resp) => {

          this.dashboard = resp;

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  //==========================================
  // CARGAR CARRERAS
  //==========================================

  cargarCarreras(): void {

    this.carreraService

      .listar()

      .subscribe({

        next: (resp) => {

          this.carreras = resp;

          this.carrerasOriginal = [...resp];

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  //==========================================
  // BUSCAR
  //==========================================

  buscar(): void {

    const texto = this.textoBusqueda

      .toLowerCase()

      .trim();

    if (texto === '') {

      this.carreras = [...this.carrerasOriginal];

      return;

    }

    this.carreras = this.carrerasOriginal.filter(c =>

      c.nombre.toLowerCase().includes(texto)

      ||

      c.abreviatura.toLowerCase().includes(texto)

    );

  }

  //==========================================
  // LIMPIAR BUSCADOR
  //==========================================

  limpiarBusqueda(): void {

    this.textoBusqueda = '';

    this.carreras = [...this.carrerasOriginal];

  }
    //==========================================
  // NUEVA CARRERA
  //==========================================

  nuevaCarrera(): void {

    this.editando = false;

   this.carrera = {

    nombre:'',

    abreviatura:'',

    totalAlumnos:0

} as Carrera;

    this.mostrarModal = true;

  }

  //==========================================
  // EDITAR
  //==========================================

  editar(carrera: Carrera): void {

    this.editando = true;

this.carrera = {

    id: carrera.id,

    nombre: carrera.nombre,

    abreviatura: carrera.abreviatura,

    totalAlumnos: carrera.totalAlumnos

};
    this.mostrarModal = true;

  }

  //==========================================
  // CERRAR MODAL
  //==========================================

  cerrarModal(): void {

    this.mostrarModal = false;

    this.editando = false;

    this.carrera = {

    nombre:'',

    abreviatura:'',

    totalAlumnos:0

} as Carrera;
  }

    //==========================================
  // GUARDAR / ACTUALIZAR
  //==========================================

  guardar(): void {

    if (

      this.carrera.nombre.trim() === '' ||

      this.carrera.abreviatura.trim() === ''

    ) {

      Swal.fire(

        'Campos incompletos',

        'Capture el nombre y la abreviatura.',

        'warning'

      );

      return;

    }

    if (this.editando) {

      this.carreraService

        .actualizar(

          this.carrera.id,

          this.carrera

        )

        .subscribe({

          next: () => {

            Swal.fire(

              'Correcto',

              'Carrera actualizada correctamente.',

              'success'

            );

            this.cerrarModal();

            this.cargarCarreras();

            this.cargarDashboard();

          },

          error: (err) => {

            console.error(err);

            Swal.fire(

              'Error',

              err.error?.message ||

              'No fue posible actualizar la carrera.',

              'error'

            );

          }

        });

    } else {

      this.carreraService

        .registrar(

          this.carrera

        )

        .subscribe({

          next: () => {

            Swal.fire(

              'Correcto',

              'Carrera registrada correctamente.',

              'success'

            );

            this.cerrarModal();

            this.cargarCarreras();

            this.cargarDashboard();

          },

          error: (err) => {

            console.error(err);

            Swal.fire(

              'Error',

              err.error?.message ||

              'No fue posible registrar la carrera.',

              'error'

            );

          }

        });

    }

  }

  //==========================================
  // ELIMINAR
  //==========================================

  eliminar(carrera: Carrera): void {

    Swal.fire({

      title: '¿Eliminar carrera?',

      text:

        'Esta acción no podrá deshacerse.',

      icon: 'warning',

      showCancelButton: true,

      confirmButtonText: 'Eliminar',

      cancelButtonText: 'Cancelar',

      confirmButtonColor: '#d33'

    }).then((result) => {

      if (!result.isConfirmed) {

        return;

      }

      this.carreraService

        .eliminar(carrera.id)

        .subscribe({

          next: () => {

            Swal.fire(

              'Correcto',

              'Carrera eliminada.',

              'success'

            );

            this.cargarCarreras();

            this.cargarDashboard();

          },

          error: (err) => {

            console.error(err);

            Swal.fire(

              'No es posible eliminar',

              err.error?.message ||

              'La carrera tiene alumnos registrados.',

              'error'

            );

          }

        });

    });

  }

}