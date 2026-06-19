import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';
import { loginGuard } from './core/guards/login-guard';

import { Login } from './pages/login/login';
import { Dashboard } from './pages/admin/dashboard/dashboard';
import { NuevoAlumno } from './pages/admin/nuevo-alumno/nuevo-alumno';
import { Alumnos } from './pages/alumnos/alumnos';
import { Carreras } from './pages/carreras/carreras';
import { Credenciales } from './pages/credenciales/credenciales';
import { Perfil } from './pages/perfil/perfil';
import { Usuarios } from './pages/usuarios/usuarios';
import { Auditoria } from './pages/auditoria/auditoria';
import { Configuracion } from './pages/configuracion/configuracion';
import { Alumnosdash} from './pages/admin/alumnosdash/alumnosdash';


export const routes: Routes = [

  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },

  {
    path: 'login',
    component: Login,
    canActivate: [loginGuard]
  },

  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [authGuard]
  },
{
  path: 'nuevo-alumno',
  component: NuevoAlumno,
  canActivate: [authGuard]
},
{
  path: 'alumnos-dash',
  component: Alumnosdash,
  canActivate: [authGuard]
},

  {
    path: 'alumnos',
    component: Alumnos,
    canActivate: [authGuard]
  },

  {
    path: 'carreras',
    component: Carreras,
    canActivate: [authGuard]
  },

  {
    path: 'credenciales',
    component: Credenciales,
    canActivate: [authGuard]
  },

  {
    path: 'perfil',
    component: Perfil,
    canActivate: [authGuard]
  },

  {
    path: 'usuarios',
    component: Usuarios,
    canActivate: [authGuard]
  },

  {
    path: 'auditoria',
    component: Auditoria,
    canActivate: [authGuard]
  },

  {
    path: 'configuracion',
    component: Configuracion,
    canActivate: [authGuard]
  },

  {
    path: '**',
    redirectTo: 'login'
  }

];