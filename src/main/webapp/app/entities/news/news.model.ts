import { BaseEntity, User } from './../../shared';
import {Comment} from '../comment/comment.model';

export class News implements BaseEntity {
    constructor(
        public description?: string,
        public title?: string,
        public imageUrl?: string,
        public id?: number,
        public urn? :string,
        public date? : string
    ) {
    }
}
