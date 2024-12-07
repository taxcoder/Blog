export interface ItemOptions {
  label: string;
  key: string;
  url: string;
}

export interface NavigationOptions {
  name: string;
  iconName: string;
  options: ItemOptions[];
  url: string;
}
