import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {
  ConfirmDialogComponent,
  ConfirmDialogConfig,
  ConfirmDialogResult,
} from '../structure/dialogs/confirm-dialog/confirm-dialog.component';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SkimaDialogService {
  constructor(private dialog: MatDialog) {}

  showConfirmDialog(config: ConfirmDialogConfig): Observable<ConfirmDialogResult> {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      data: config,
    });

    return dialogRef.afterClosed();
  }

  async showConfirmDialog$(config: ConfirmDialogConfig): Promise<ConfirmDialogResult> {
    return new Promise((resolve) => {
      this.showConfirmDialog(config).subscribe((result: ConfirmDialogResult) => {
        resolve(result);
      });
    });
  }
}
