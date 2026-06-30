import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { StorageService } from '../services/storage.service';

export const loginGuard: CanActivateFn = () => {

  const storage = inject(StorageService);

  const router = inject(Router);

  if (storage.estaLogueado()) {

    router.navigate(['/admin/dashboard']);

    return false;

  }

  return true;

};