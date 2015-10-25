package com.shenhui.doubanfilm.bean;

import java.util.List;

public class CelebrityBean {

    private String mobile_url;//条目移动版url
    private String name;//中文名
    private String name_en;//英文名
    private String gender;//性别
    private AvatarsEntity avatars;//影人头像
    private String alt;//条目页url
    private String born_place;//出生地
    private String id;//条目id
    private List<String> aka_en;//更多英文名
    private List<String> aka;//更多中文名
    private List<WorksEntity> works;//影人作品,最多五部

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAvatars(AvatarsEntity avatars) {
        this.avatars = avatars;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setBorn_place(String born_place) {
        this.born_place = born_place;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAka_en(List<String> aka_en) {
        this.aka_en = aka_en;
    }

    public void setAka(List<String> aka) {
        this.aka = aka;
    }

    public void setWorks(List<WorksEntity> works) {
        this.works = works;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public String getName() {
        return name;
    }

    public String getName_en() {
        return name_en;
    }

    public String getGender() {
        return gender;
    }

    public AvatarsEntity getAvatars() {
        return avatars;
    }

    public String getAlt() {
        return alt;
    }

    public String getBorn_place() {
        return born_place;
    }

    public String getId() {
        return id;
    }

    public List<String> getAka_en() {
        return aka_en;
    }

    public List<String> getAka() {
        return aka;
    }

    public List<WorksEntity> getWorks() {
        return works;
    }

    public static class AvatarsEntity {

        private String small;
        private String large;
        private String medium;

        public void setSmall(String small) {
            this.small = small;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getSmall() {
            return small;
        }

        public String getLarge() {
            return large;
        }

        public String getMedium() {
            return medium;
        }
    }

    public static class WorksEntity {

        private SubjectEntity subject;
        private List<String> roles;

        public void setSubject(SubjectEntity subject) {
            this.subject = subject;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public SubjectEntity getSubject() {
            return subject;
        }

        public List<String> getRoles() {
            return roles;
        }

        public static class SubjectEntity {

            private RatingEntity rating;
            private int collect_count;
            private String title;
            private String original_title;
            private String subtype;
            private String year;
            private ImagesEntity images;
            private String alt;
            private String id;
            private List<String> genres;
            private List<CastsEntity> casts;
            private List<DirectorsEntity> directors;

            public void setRating(RatingEntity rating) {
                this.rating = rating;
            }

            public void setCollect_count(int collect_count) {
                this.collect_count = collect_count;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setOriginal_title(String original_title) {
                this.original_title = original_title;
            }

            public void setSubtype(String subtype) {
                this.subtype = subtype;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public void setImages(ImagesEntity images) {
                this.images = images;
            }

            public void setAlt(String alt) {
                this.alt = alt;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setGenres(List<String> genres) {
                this.genres = genres;
            }

            public void setCasts(List<CastsEntity> casts) {
                this.casts = casts;
            }

            public void setDirectors(List<DirectorsEntity> directors) {
                this.directors = directors;
            }

            public RatingEntity getRating() {
                return rating;
            }

            public int getCollect_count() {
                return collect_count;
            }

            public String getTitle() {
                return title;
            }

            public String getOriginal_title() {
                return original_title;
            }

            public String getSubtype() {
                return subtype;
            }

            public String getYear() {
                return year;
            }

            public ImagesEntity getImages() {
                return images;
            }

            public String getAlt() {
                return alt;
            }

            public String getId() {
                return id;
            }

            public List<String> getGenres() {
                return genres;
            }

            public List<CastsEntity> getCasts() {
                return casts;
            }

            public List<DirectorsEntity> getDirectors() {
                return directors;
            }

            public static class RatingEntity {

                private int max;
                private double average;
                private String stars;
                private int min;

                public void setMax(int max) {
                    this.max = max;
                }

                public void setAverage(double average) {
                    this.average = average;
                }

                public void setStars(String stars) {
                    this.stars = stars;
                }

                public void setMin(int min) {
                    this.min = min;
                }

                public int getMax() {
                    return max;
                }

                public double getAverage() {
                    return average;
                }

                public String getStars() {
                    return stars;
                }

                public int getMin() {
                    return min;
                }
            }

            public static class ImagesEntity {

                private String small;
                private String large;
                private String medium;

                public void setSmall(String small) {
                    this.small = small;
                }

                public void setLarge(String large) {
                    this.large = large;
                }

                public void setMedium(String medium) {
                    this.medium = medium;
                }

                public String getSmall() {
                    return small;
                }

                public String getLarge() {
                    return large;
                }

                public String getMedium() {
                    return medium;
                }
            }

            public static class CastsEntity {

                private Object avatars;
                private Object alt;
                private Object id;
                private String name;

                public void setAvatars(Object avatars) {
                    this.avatars = avatars;
                }

                public void setAlt(Object alt) {
                    this.alt = alt;
                }

                public void setId(Object id) {
                    this.id = id;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Object getAvatars() {
                    return avatars;
                }

                public Object getAlt() {
                    return alt;
                }

                public Object getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }
            }

            public static class DirectorsEntity {

                private Object avatars;
                private Object alt;
                private Object id;
                private String name;

                public void setAvatars(Object avatars) {
                    this.avatars = avatars;
                }

                public void setAlt(Object alt) {
                    this.alt = alt;
                }

                public void setId(Object id) {
                    this.id = id;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Object getAvatars() {
                    return avatars;
                }

                public Object getAlt() {
                    return alt;
                }

                public Object getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }
            }
        }
    }
}
