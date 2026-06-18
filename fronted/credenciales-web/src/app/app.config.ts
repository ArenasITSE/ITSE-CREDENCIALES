import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
} from '@angular/core';

import {
  provideRouter
} from '@angular/router';

import {
  provideHttpClient,
  withInterceptors
} from '@angular/common/http';

import {
  provideAnimations
} from '@angular/platform-browser/animations';

import {
  provideCharts,
  withDefaultRegisterables
} from 'ng2-charts';

import {
  routes
} from './app.routes';

import {
  jwtInterceptor
} from './core/interceptors/jwt-interceptor';

export const appConfig: ApplicationConfig = {

  providers: [

    provideBrowserGlobalErrorListeners(),

    provideZoneChangeDetection({

      eventCoalescing: true

    }),

    provideRouter(routes),

    provideAnimations(),

    provideCharts(
      withDefaultRegisterables()
    ),

    provideHttpClient(

      withInterceptors([

        jwtInterceptor

      ])

    )

  ]

};