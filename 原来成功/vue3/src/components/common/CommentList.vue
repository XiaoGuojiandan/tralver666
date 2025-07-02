<template>
  <div class="comment-list">
    <!-- 发表评论区域 -->
    <div class="comment-form" v-if="userStore.isLoggedIn">
      <div class="form-header">
        <el-avatar :size="40" :src="userStore.userInfo?.avatar || '/src/assets/images/no-image.png'" />
        <div class="rating-wrapper" v-if="props.targetType === 'food'">
          <span class="label">评分：</span>
          <el-rate v-model="newComment.rating" />
        </div>
      </div>
      <div class="form-content">
        <el-input
          v-model="newComment.content"
          type="textarea"
          :rows="3"
          placeholder="写下你的评论..."
        />
        <div class="form-footer">
          <el-button type="primary" @click="submitComment">发表评论</el-button>
        </div>
      </div>
    </div>
    <div v-else class="login-tip">
      <el-alert
        title="请登录后发表评论"
        type="info"
        :closable="false"
        center
      />
    </div>

    <!-- 评论列表 -->
    <div v-if="comments.length > 0">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <div class="comment-header">
          <div class="user-info">
            <el-avatar :size="40" :src="comment.userAvatar || '/src/assets/images/no-image.png'" />
            <div class="user-meta">
              <span class="username">{{ comment.userNickname || '匿名用户' }}</span>
              <span class="time">{{ formatTime(comment.createTime) }}</span>
            </div>
          </div>
          <div class="rating" v-if="comment.rating">
            <el-rate v-model="comment.rating" disabled show-score />
          </div>
        </div>
        <div class="comment-content">
          {{ comment.content }}
        </div>
        <div class="comment-footer">
          <div class="actions">
            <span class="like-action" @click="handleLike(comment)" v-if="props.targetType === 'scenic'">
              <el-icon :class="{ active: comment.isLiked }"><ThumbsUp /></el-icon>
              {{ comment.likes || 0 }}
            </span>
            <span class="reply-action" @click="showReplyInput(comment)" v-if="props.targetType === 'scenic'">
              <el-icon><ChatLineRound /></el-icon>
              回复
            </span>
          </div>
        </div>
        <!-- 回复列表 -->
        <div v-if="props.targetType === 'scenic' && comment.replies && comment.replies.length > 0" class="reply-list">
          <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
            <div class="reply-header">
              <div class="user-info">
                <el-avatar :size="30" :src="reply.userAvatar || '/src/assets/images/no-image.png'" />
                <div class="user-meta">
                  <span class="username">{{ reply.userNickname || '匿名用户' }}</span>
                  <span class="time">{{ formatTime(reply.createTime) }}</span>
                </div>
              </div>
            </div>
            <div class="reply-content">
              {{ reply.content }}
            </div>
          </div>
        </div>
        <!-- 回复输入框 -->
        <div v-if="props.targetType === 'scenic' && comment.showReplyInput" class="reply-input">
          <el-input
            v-model="comment.replyContent"
            type="textarea"
            :rows="2"
            placeholder="写下你的回复..."
          />
          <div class="reply-actions">
            <el-button size="small" @click="hideReplyInput(comment)">取消</el-button>
            <el-button type="primary" size="small" @click="submitReply(comment)">回复</el-button>
          </div>
        </div>
      </div>
    </div>
    <!-- 无评论时显示 -->
    <el-empty v-else description="暂无评论" />
    
    <!-- 分页 -->
    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ThumbsUp, ChatLineRound } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { formatTime } from '@/utils/dateUtils'
import { getComments } from '@/api/comment'
import { getCommentList, addComment as addFoodComment, likeComment as likeFoodComment } from '@/api/food'
import { useUserStore } from '@/store/user'

const props = defineProps({
  targetId: {
    type: [String, Number],
    required: true
  },
  targetType: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['refresh'])

const userStore = useUserStore()
const comments = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 新评论数据
const newComment = ref({
  content: '',
  rating: 5
})

// 加载评论列表
const loadComments = async () => {
  try {
    let res;
    if (props.targetType === 'food') {
      res = await getCommentList(props.targetId, {
        pageNum: currentPage.value,
        pageSize: pageSize.value
      });
    } else if (props.targetType === 'scenic') {
      res = await getComments({
        targetId: props.targetId,
        targetType: props.targetType,
        currentPage: currentPage.value,
        size: pageSize.value
      });
    }

    if (res.code === '200') {
      comments.value = res.data.records;
      total.value = res.data.total;
    } else {
      ElMessage.error(res.msg || '加载评论失败');
    }
  } catch (error) {
    ElMessage.error('加载评论失败');
    console.error('加载评论失败:', error);
  }
}

// 处理点赞
const handleLike = async (comment) => {
  if (props.targetType !== 'scenic') return;
  
  try {
    const res = await likeFoodComment(comment.id, userStore.userInfo.id);
    if (res.code === '200') {
      comment.isLiked = !comment.isLiked;
      comment.likes = (comment.likes || 0) + (comment.isLiked ? 1 : -1);
      ElMessage.success(comment.isLiked ? '点赞成功' : '取消点赞成功');
    } else {
      ElMessage.error(res.msg || '操作失败');
    }
  } catch (error) {
    ElMessage.error('操作失败');
    console.error('点赞操作失败:', error);
  }
}

// 显示回复输入框
const showReplyInput = (comment) => {
  if (props.targetType !== 'scenic') return;
  comment.showReplyInput = true;
  comment.replyContent = '';
}

// 隐藏回复输入框
const hideReplyInput = (comment) => {
  if (props.targetType !== 'scenic') return;
  comment.showReplyInput = false;
  comment.replyContent = '';
}

// 提交回复
const submitReply = async (comment) => {
  if (props.targetType !== 'scenic') return;

  if (!comment.replyContent?.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    const res = await addFoodComment({
      foodId: props.targetId,
      userId: userStore.userInfo.id,
      content: comment.replyContent,
      rating: 5 // 默认评分
    });

    if (res.code === '200') {
      ElMessage.success('回复成功');
      hideReplyInput(comment);
      loadComments(); // 重新加载评论列表
    } else {
      ElMessage.error(res.msg || '提交回复失败');
    }
  } catch (error) {
    ElMessage.error('提交回复失败');
    console.error('提交回复失败:', error);
  }
}

// 提交评论
const submitComment = async () => {
  if (!newComment.value.content?.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  try {
    let res;
    if (props.targetType === 'food') {
      res = await addFoodComment({
        foodId: props.targetId,
        userId: userStore.userInfo.id,
        content: newComment.value.content,
        rating: newComment.value.rating
      });
    } else if (props.targetType === 'scenic') {
      res = await submitReply({
        targetId: props.targetId,
        targetType: props.targetType,
        content: newComment.value.content
      });
    }

    if (res.code === '200') {
      ElMessage.success('评论成功');
      // 清空评论内容
      newComment.value.content = '';
      if (props.targetType === 'food') {
        newComment.value.rating = 5;
      }
      // 重新加载评论列表
      loadComments();
      // 触发刷新事件
      emit('refresh');
    } else {
      ElMessage.error(res.msg || '评论失败');
    }
  } catch (error) {
    ElMessage.error('评论失败');
    console.error('评论失败:', error);
  }
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  loadComments()
}

// 当前页改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  loadComments()
}

onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.comment-list {
  padding: 20px 0;
}

.comment-item {
  margin-bottom: 20px;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-meta {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: 500;
  color: #333;
}

.time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  margin: 10px 0;
  color: #666;
  line-height: 1.6;
}

.comment-footer {
  margin-top: 10px;
}

.actions {
  display: flex;
  gap: 20px;
}

.like-action,
.reply-action {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  color: #666;
}

.like-action:hover,
.reply-action:hover {
  color: #409eff;
}

.like-action .active {
  color: #409eff;
}

.reply-list {
  margin: 10px 0 0 50px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
}

.reply-item {
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.reply-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.reply-header {
  margin-bottom: 5px;
}

.reply-content {
  margin-left: 40px;
  color: #666;
}

.reply-input {
  margin-top: 10px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.comment-form {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

  .form-header {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
    gap: 16px;

    .rating-wrapper {
      display: flex;
      align-items: center;
      gap: 8px;

      .label {
        color: #606266;
      }
    }
  }

  .form-content {
    .el-textarea {
      margin-bottom: 16px;
    }

    .form-footer {
      display: flex;
      justify-content: flex-end;
    }
  }
}

.login-tip {
  margin-bottom: 24px;
}
</style> 