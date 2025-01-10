import { Component, inject } from '@angular/core';

import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { MatIcon } from '@angular/material/icon';
import { AutoUnsubscribable, AutoUnsubscribe } from '../../../../../_shared/utils/auto-unsubscribe';
import { SubscriptionUtil } from '../../../../../_shared/utils/subscription-util';
import { MatFormFieldModule } from '@angular/material/form-field';
import { SkimaValidators } from '../../../../../_shared/validators/skima-validators';
import { SkillTopicDto } from '../../../../../api/model/api.types';
import { SkillTopicApi } from '../../../../../api/services/skill-topic-api.service';
import { ErrorUtil } from '../../../../../_shared/utils/error-util';
import { SkimaToastService } from '../../../../../_shared/services/skima-toast.service';
import { Router } from '@angular/router';
import { SkimaNavigator } from '../../../../../_shared/services/skima-navigator.service';

export interface SkillForm {
  title: FormControl<string>;
  certifications: FormArray<FormControl<string>>;
}

@Component({
  selector: 'skima-create-skill-form',
  templateUrl: './create-skill-form.component.html',
  styleUrl: './create-skill-form.component.scss',
  standalone: true,
  imports: [
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    ReactiveFormsModule,
    MatIcon,
  ],
})
@AutoUnsubscribe
export class CreateSkillForm implements AutoUnsubscribable {
  private fb = inject(FormBuilder);

  skillForm: FormGroup<SkillForm> = this.fb.group({
    title: this.fb.nonNullable.control('', SkimaValidators.notEmpty),
    certifications: this.fb.array<FormControl<string>>([]),
  });

  subs = new SubscriptionUtil();

  constructor(
    private skillTopicApi: SkillTopicApi,
    private toastService: SkimaToastService,
    private navigator: SkimaNavigator
  ) {
    this.addCertification();
  }

  get certifications(): FormArray<FormControl<string>> {
    return this.skillForm.get('certifications') as FormArray;
  }

  addCertification() {
    this.certifications.push(this.fb.nonNullable.control('', SkimaValidators.notEmpty));
  }

  removeCertification(index: number) {
    this.certifications.removeAt(index);
  }

  onSubmit(): void {
    const newSkill: SkillTopicDto = this.skillForm.value as SkillTopicDto;

    if (!this.skillForm.valid) return;

    this.skillTopicApi.createSkillTopic(newSkill).subscribe({
      next: (createdSkillTopic) => {
        this.navigator.router.navigateByUrl(this.navigator.linkSkillTopic(createdSkillTopic.id!));
      },
      error: (err) => {
        const userError = ErrorUtil.extractUserError(err);
        if (userError && userError.errorCode === 'ALREADY_EXISTS')
          this.toastService.showError('You already created a Skill with this Title!');
      },
    });
  }
}
