import { Injectable } from '@angular/core';
import { Router, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class SkimaNavigator {
  /**
   *
   * @param router router is public for convenience when classes that inject the Navigator can use it
   *
   */
  constructor(
    public router: Router
    // /!\ Note: you CANNOT inject ActivatedRoute here /!\ Since you are in a service context it will be empty!
  ) {}

  /**
   * Opens the given targetUrl, but with rewriting of the last browser-history entry.
   * This way, your automatic redirections won't break the browser's back-button.
   */
  redirectTo(targetUrl: string) {
    this.router.navigateByUrl(targetUrl, { replaceUrl: true });
  }

  linkSkillTopic(skillTopicId: number): string {
    return `/skills/${skillTopicId}`;
  }

  linkCreateActivity(skillTopicId: number): string {
    return `/skills/${skillTopicId}/create-activity`;
  }
}
