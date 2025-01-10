import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButton } from '@angular/material/button';


export interface ConfirmDialogConfig {
  title: string;
  message: string;
  showCancel?: boolean;
}
export interface ConfirmDialogResult {
  result: 'confirm'|'deny'|'cancel';
}

@Component({
  selector: 'skima-confirm-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButton],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.scss',
})
export class ConfirmDialogComponent {


  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public config: ConfirmDialogConfig,
  ) {
    if (this.config.showCancel == null) this.config.showCancel = false;
  }

  onNoClick(): void {
    const result: ConfirmDialogResult = {
      result: 'deny'
    };
    this.dialogRef.close(result);
  }

  onYesClick(): void {
    const result: ConfirmDialogResult = {
      result: 'confirm'
    };
    this.dialogRef.close(result);
  }

  onCancelClick() {
    const result: ConfirmDialogResult = {
      result: 'cancel'
    };
    this.dialogRef.close(result);
  }
}
