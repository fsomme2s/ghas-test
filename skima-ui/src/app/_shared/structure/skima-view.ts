import { AutoUnsubscribable, AutoUnsubscribe } from '../utils/auto-unsubscribe';
import { SubscriptionUtil } from '../utils/subscription-util';
import { SkimaNavigator } from '../services/skima-navigator.service';
import { Title } from '@angular/platform-browser';
import { inject } from '@angular/core';

@AutoUnsubscribe
export abstract class SkimaView implements AutoUnsubscribable {
  private title: Title = inject(Title);

  subs = new SubscriptionUtil();

  constructor(protected navigator: SkimaNavigator /*protected breadcrumbService: BreadcrumbService, protected logger: Logger*/) {
    // Development Log for orientation:
    console.log('View: ', this.constructor.name);
    // this.breadcrumbService.setGenericBreadcrumb([])
  }

  updateViewTitle(title: string) {
    this.title.setTitle(title + ' - Skill Manager');
  }
}
