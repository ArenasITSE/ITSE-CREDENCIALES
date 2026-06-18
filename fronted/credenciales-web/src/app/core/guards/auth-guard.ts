import { CanActivateFn } from '@angular/router';

import { inject } from '@angular/core';

import { Router } from '@angular/router';

import { StorageService } from '../services/storage.service';

export const authGuard: CanActivateFn = () => {

  const storage = inject(StorageService);

  const router = inject(Router);

  if (storage.estaLogueado()) {

    return true;

  }

  router.navigate(['/login']);

  return false;

};