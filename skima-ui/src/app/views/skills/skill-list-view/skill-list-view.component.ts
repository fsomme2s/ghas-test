import { Component } from '@angular/core';
import { SkimaView } from '../../../_shared/structure/skima-view';
import { SkillTopicApi } from '../../../api/services/skill-topic-api.service';
import { SkillTopicDto } from '../../../api/model/api.types';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { NgIf } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatIcon } from '@angular/material/icon';
import { MatFabButton } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { SkimaNavigator } from '../../../_shared/services/skima-navigator.service';

@Component({
  selector: 'skima-skill-list-view',
  standalone: true,
  imports: [MatProgressSpinner, NgIf, MatTableModule, MatIcon, MatFabButton, RouterLink],
  templateUrl: './skill-list-view.component.html',
  styleUrl: './skill-list-view.component.scss',
})
export class SkillListView extends SkimaView {
  skillTopics: SkillTopicDto[] | null = null; // TODO use fancy datasource object

  displayedColumns: string[] = ['title'];

  constructor(
    private skillTopicApi: SkillTopicApi,
    skimaNavigator: SkimaNavigator
  ) {
    super(skimaNavigator);
    this.subs.add(
      skillTopicApi.getSkillTopics().subscribe((skillTopics) => (this.skillTopics = skillTopics))
    );
  }

  createSkill() {
    this.navigator.router.navigateByUrl('/create-skill');
  }

  linkSkill(skillTopic: SkillTopicDto): string {
    return this.navigator.linkSkillTopic(skillTopic.id!);
  }
}
