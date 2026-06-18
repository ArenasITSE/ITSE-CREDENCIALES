
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

import { BaseChartDirective } from 'ng2-charts';

import {
  ChartConfiguration,
  ChartType
} from 'chart.js';

import { DashboardService } from '../../../core/services/dashboard.service';
import { StorageService } from '../../../core/services/storage.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    BaseChartDirective
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {

  private dashboardService = inject(DashboardService);
  private storage = inject(StorageService);
  private router = inject(Router);

  cargando = true;

  //======================================
  // USUARIO
  //======================================

  usuario = {

    nombre: 'Administrador',

    foto: ''

  };

  //======================================
  // DASHBOARD
  //======================================

  dashboard: any = {

    totalAlumnos: 0,

    totalCarreras: 0,

    totalCredenciales: 0,

    credencialesValidadas: 0,

    credencialesCanceladas: 0

  };

  //======================================
  // TABLAS
  //======================================

  ultimosAlumnos: any[] = [];

  ultimasCredenciales: any[] = [];

  //======================================
  // GRÁFICAS
  //======================================

  doughnutChartType: ChartType = 'doughnut';

  doughnutChartOptions: ChartConfiguration<'doughnut'>['options'] = {

    responsive: true,

    maintainAspectRatio: false,

    plugins: {

      legend: {

        position: 'bottom'

      }

    }

  };

  alumnosChartData: ChartConfiguration<'doughnut'>['data'] = {

    labels: [],

    datasets: [

      {

        data: []

      }

    ]

  };

  credencialesChartData: ChartConfiguration<'doughnut'>['data'] = {

    labels: [],

    datasets: [

      {

        data: []

      }

    ]

  };

  //======================================
  // INIT
  //======================================

  ngOnInit(): void {

    this.cargarDashboard();

  }

  //======================================
  // CARGAR DASHBOARD
  //======================================

  cargarDashboard(): void {

    this.dashboardService.obtenerDashboard()

      .subscribe({

        next: (resp: any) => {

          console.log(resp);

          this.dashboard = resp;

          this.ultimosAlumnos = resp.ultimosAlumnos ?? [];

          this.ultimasCredenciales = resp.ultimasCredenciales ?? [];

          //======================================
          // ALUMNOS POR CARRERA
          //======================================

          this.alumnosChartData = {

            labels: (resp.alumnosPorCarrera ?? []).map(

              (x: any) => x.carrera

            ),

            datasets: [

              {

                data: (resp.alumnosPorCarrera ?? []).map(

                  (x: any) => x.cantidad

                )

              }

            ]

          };

          //======================================
          // CREDENCIALES POR ESTADO
          //======================================

          this.credencialesChartData = {

            labels: (resp.credencialesPorEstado ?? []).map(

              (x: any) => x.estado

            ),

            datasets: [

              {

                data: (resp.credencialesPorEstado ?? []).map(

                  (x: any) => x.cantidad

                )

              }

            ]

          };

          this.cargando = false;

        },

        error: (err) => {

          console.error(err);

          this.cargando = false;

        }

      });

  }

  //======================================
  // LOGOUT
  //======================================

  cerrarSesion(): void {

    this.storage.logout();

    this.router.navigate([
      '/login'
    ]);

  }

}