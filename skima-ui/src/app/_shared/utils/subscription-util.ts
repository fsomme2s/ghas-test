import { Subscription } from 'rxjs';

/**
 * Convenience tool for gathering several subscriptions
 * and finally unsubscribe() all of them (Usually happens in onDestroy).
 */
export class SubscriptionUtil {
  subscriptions: Subscription[] = [];

  /**
   * Can handle null values in the list
   */
  add(...subs: Subscription[]) {
    if (subs != null && subs.length > 0) {
      this.subscriptions.push(...subs);
    }
  }

  /**
   * Clears all subscriptions.
   */
  clear() {
    for (const sub of this.subscriptions) {
      if (sub != null) {
        sub.unsubscribe();
      }
    }
  }
}
