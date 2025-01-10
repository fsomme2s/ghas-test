import { Component, inject, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { SkimaValidators } from '../../../../../_shared/validators/skima-validators';
import { SkimaNavigator } from '../../../../../_shared/services/skima-navigator.service';
import {
  ActivityMetric,
  SkillTopicDto,
  TrainingActivityDto,
} from '../../../../../api/model/api.types';
import {
  createFormArrayPrimitive,
  createFormGroup,
  ModelFormGroup,
} from '../../../../../_shared/utils/form-utils';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatIcon } from '@angular/material/icon';
import {
  EnumOption,
  EnumOptionMap,
  EnumTranslatorService,
  toMap,
} from '../../../../../api/extension/enum-translator.service';
import { TranslateModule } from '@ngx-translate/core';
import { SkimaView } from '../../../../../_shared/structure/skima-view';
import { SkillTopicApi } from '../../../../../api/services/skill-topic-api.service';
import { NgIf } from '@angular/common';
import { TrainingActivityApi } from '../../../../../api/services/training-activity-api.service';

@Component({
  selector: 'skima-create-activity-form',
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
    TranslateModule,
    NgIf,
  ],
  templateUrl: './create-activity-form.component.html',
  styleUrl: './create-activity-form.component.scss',
})
export class CreateActivityForm extends SkimaView implements OnInit {
  @Input()
  skillTopicId!: number;

  skillTopic?: SkillTopicDto;

  private fb = inject(FormBuilder);

  activityForm: ModelFormGroup<TrainingActivityDto> = createFormGroup<TrainingActivityDto>({
    title: this.fb.nonNullable.control('', SkimaValidators.notEmpty),
    metric: this.fb.nonNullable.control<ActivityMetric>('CHAPTERS'),
    targetAmount: this.fb.nonNullable.control(0.0),
    subTasks: createFormArrayPrimitive<string>(),
  });

  activityMetricOptions: EnumOption<ActivityMetric>[];
  activityMetricOptionsItems: EnumOptionMap<ActivityMetric>;
  activityMetricOptionsItemsSingular: EnumOptionMap<ActivityMetric>;

  constructor(
    navigator: SkimaNavigator,
    private enumTranslatorService: EnumTranslatorService,
    private skillTopicApi: SkillTopicApi,
    private trainingActivityApi: TrainingActivityApi,
  ) {
    super(navigator);
    this.activityMetricOptions = this.enumTranslatorService.activityMetric();
    this.activityMetricOptionsItems = toMap(this.enumTranslatorService.activityMetric('items'));
    this.activityMetricOptionsItemsSingular = toMap(
      this.enumTranslatorService.activityMetric('items_singular'),
    );
  }

  async ngOnInit() {
    this.skillTopic = await this.skillTopicApi.getSkillTopicDetails$(this.skillTopicId);
    this.updateViewTitle(`Plan activity for "${this.skillTopic.title}"`);
  }

  get selectedMetric() {
    return this.activityForm.controls.metric.value;
  }

  //
  // +++ Subtasks +++
  //

  showSubTaskList(): boolean {
    return ['TODOS', 'WEB_PAGES'].includes(this.activityForm.controls.metric.value);
  }

  get subTasks(): FormArray<FormControl<string>> {
    return this.activityForm.controls.subTasks;
  }

  removeSubtask(i: number) {
    this.subTasks.removeAt(i);
  }

  addSubtask() {
    this.subTasks.push(this.fb.nonNullable.control(''));
  }

  //
  // /Subtasks
  //

  async onSubmit() {
    const activity: TrainingActivityDto = this.activityForm.value as TrainingActivityDto;

    console.log(activity);
    if (!this.activityForm.valid) return;
    await this.trainingActivityApi.createActivity$(this.skillTopicId, activity);

    this.navigator.router.navigateByUrl(this.navigator.linkSkillTopic(this.skillTopicId));
  }
}
