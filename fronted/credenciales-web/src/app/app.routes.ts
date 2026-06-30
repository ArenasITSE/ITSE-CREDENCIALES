import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';
import { loginGuard } from './core/guards/login-guard';

// Layout
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';

// Login
import { Login } from './pages/login/login';

// Admin
import { Dashboard } from './pages/admin/dashboard/dashboard';
import { NuevoAlumno } from './pages/admin/nuevo-alumno/nuevo-alumno';
import { Alumnosdash } from './pages/admin/alumnosdash/alumnosdash';

// Módulos
import { Alumnos } from './pages/alumnos/alumnos';
import { Carreras } from './pages/carreras/carreras';
import { Credenciales } from './pages/credenciales/credenciales';
import { Perfil } from './pages/perfil/perfil';
import { Usuarios } from './pages/usuarios/usuarios';
import { Auditoria } from './pages/auditoria/auditoria';
import { Configuracion } from './pages/configuracion/configuracion';

export const routes: Routes = [

    {
        path:'',
        redirectTo:'login',
        pathMatch:'full'
    },

    {
        path:'login',
        component:Login,
        canActivate:[loginGuard]
    },

    //------------------------------------------------
    // PANEL ADMINISTRADOR
    //------------------------------------------------

    {

        path:'admin',

        component:AdminLayoutComponent,

        canActivate:[authGuard],

        children:[

            {
                path:'',
                redirectTo:'dashboard',
                pathMatch:'full'
            },

            {
                path:'dashboard',
                component:Dashboard
            },

            {
                path:'nuevo-alumno',
                component:NuevoAlumno
            },

            {
                path:'alumnos',
                component:Alumnosdash
            },

            {
                path:'lista-alumnos',
                component:Alumnos
            },

            {
                path:'credenciales',
                component:Credenciales
            },

            {
                path:'carreras',
                component:Carreras
            },

            {
                path:'usuarios',
                component:Usuarios
            },

            {
                path:'auditoria',
                component:Auditoria
            },

            {
                path:'configuracion',
                component:Configuracion
            }

        ]

    },

    //------------------------------------------------
    // PORTAL DEL ALUMNO
    //------------------------------------------------

    {

        path:'credencial-alumnos',

        component:Alumnos,

        canActivate:[authGuard]

    },

    //------------------------------------------------

    {

        path:'**',

        redirectTo:'login'

    }

];