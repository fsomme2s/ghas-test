import { Component, inject, Input, OnInit } from '@angular/core';
import { SkimaView } from '../../../_shared/structure/skima-view';
import { SkimaNavigator } from '../../../_shared/services/skima-navigator.service';
import { SkillTopicApi } from '../../../api/services/skill-topic-api.service';
import { MatCell } from '@angular/material/table';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatError, MatFormFieldModule } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatButton, MatIconButton, MatMiniFabButton } from '@angular/material/button';
import { SkimaDialogService } from '../../../_shared/services/skima-dialog.service';
import { TrainingActivityApi } from '../../../api/services/training-activity-api.service';
import { ActivityMetric, SkillTopicDto, TrainingActivityDto } from '../../../api/model/api.types';
import {
  EnumOptionMap,
  EnumTranslatorService,
  toMap,
} from '../../../api/extension/enum-translator.service';

@Component({
  selector: 'skima-skill-detail-view',
  standalone: true,
  imports: [
    MatCell,
    MatProgressSpinner,
    RouterLink,
    MatIcon,
    MatCardModule,
    ReactiveFormsModule,
    MatError,
    MatFormFieldModule,
    MatInput,
    MatIconButton,
    MatButton,
    MatMiniFabButton,
  ],
  templateUrl: './skill-detail-view.component.html',
  styleUrl: './skill-detail-view.component.scss',
})
export class SkillDetailView extends SkimaView implements OnInit {
  @Input()
  skillTopicId!: number;

  skillTopic?: SkillTopicDto;
  activities?: TrainingActivityDto[];

  private fb = inject(FormBuilder);

  activityMetricOptionsItems: EnumOptionMap<ActivityMetric>;

  constructor(
    skimaNavigator: SkimaNavigator,
    private skillTopicApi: SkillTopicApi,
    private trainingActivityApi: TrainingActivityApi,
    private dialogService: SkimaDialogService,
    private enumTranslatorService: EnumTranslatorService,
  ) {
    super(skimaNavigator);
    this.activityMetricOptionsItems = toMap(this.enumTranslatorService.activityMetric('items'));
  }

  async ngOnInit(): Promise<void> {
    await this.refresh();
  }

  private async refresh() {
    this.skillTopic = await this.skillTopicApi.getSkillTopicDetails$(this.skillTopicId);
    this.updateViewTitle(`Skill "${this.skillTopic.title}"`);

    this.activities = await this.trainingActivityApi.getActiviesOfSkillTopic$(this.skillTopicId);
  }

  async removeActivity(activity: TrainingActivityDto) {
    const confirmResult = await this.dialogService.showConfirmDialog$({
      title: `Delete Activity "${activity.title}"`,
      message: 'Are you sure you want to delete this activity?',
    });
    if (confirmResult.result === 'confirm') {
      await this.trainingActivityApi.deleteActivy$(this.skillTopicId, activity.id!);
      await this.refresh();
    }
  }

  hasSubTaskList(metric: ActivityMetric): boolean {
    return ['TODOS', 'WEB_PAGES'].includes(metric);
  }

  createActivity() {
    this.navigator.router.navigateByUrl(this.navigator.linkCreateActivity(this.skillTopicId));
  }
}
