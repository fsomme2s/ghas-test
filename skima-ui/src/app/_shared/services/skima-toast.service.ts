import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SkimaToastService {
  constructor(private matSnackBar: MatSnackBar) {

  }

  showError(message: string) {
    this.matSnackBar.open(message, 'Close', {}); // TODO make it look like an error
  }
}
