import { Routes } from '@angular/router';
import { SkillListView } from './views/skills/skill-list-view/skill-list-view.component';
import { LoginView } from './views/misc/login-view/login-view.component';
import { CreateSkillForm } from './views/skills/skill-list-view/components/create-skill-form/create-skill-form.component';
import { SkillDetailView } from './views/skills/skill-detail-view/skill-detail-view.component';
import {
  CreateActivityForm
} from './views/skills/skill-detail-view/components/create-activity-form/create-activity-form.component';

// Keep consistent with SkimaNavigator

export const routes: Routes = [
  { path: 'login', title: 'Login', component: LoginView },
  { path: '', redirectTo: 'skill-list', pathMatch: 'full' },
  { path: 'skill-list', title: 'Skill List - Skill Manager', component: SkillListView },
  { path: 'create-skill', title: 'Add a Skill - Skill Manager', component: CreateSkillForm },
  { path: 'skills/:skillTopicId', title: 'Skill - Skill Manager', component: SkillDetailView },
  { path: 'skills/:skillTopicId/create-activity', title: 'Add an Activity - Skill Manager', component: CreateActivityForm },
];
