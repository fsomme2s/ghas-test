import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { SkimaRootComponent } from './app/skima-root.component';
import { registerLocaleData } from '@angular/common';


// registerLocaleData(localeDe, 'de-DE', localeDeExtra);
// registerLocaleData(localeEn, 'en-EN');

bootstrapApplication(SkimaRootComponent, appConfig)
  .catch((err) => console.error(err));
